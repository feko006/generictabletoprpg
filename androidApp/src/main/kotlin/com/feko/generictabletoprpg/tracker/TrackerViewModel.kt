package com.feko.generictabletoprpg.tracker

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.INamed
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.common.SmartNamedSearchComparator
import com.feko.generictabletoprpg.common.alertdialog.AlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.composable.InputFieldData
import com.feko.generictabletoprpg.common.fabdropdown.FabDropdownSubViewModel
import com.feko.generictabletoprpg.common.fabdropdown.IFabDropdownSubViewModel
import com.feko.generictabletoprpg.common.toast.IToastSubViewModel
import com.feko.generictabletoprpg.common.toast.ToastSubViewModel
import com.feko.generictabletoprpg.import.IJson
import com.feko.generictabletoprpg.searchall.ISearchAllUseCase
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.tracker.dialogs.IAlertDialogTrackerViewModel.DialogType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackerViewModel(
    private val groupId: Long,
    private val groupName: String,
    private val trackedThingDao: TrackedThingDao,
    private val spellDao: SpellDao,
    private val json: IJson,
    searchAllUseCase: ISearchAllUseCase
) : OverviewViewModel<Any>(trackedThingDao),
    ITrackerViewModel {

    private val _alertDialog = AlertDialogSubViewModel(viewModelScope, ::onAlertDialogDismissed)
    override val alertDialog: IAlertDialogSubViewModel = _alertDialog

    private val _fabDropdown = FabDropdownSubViewModel(viewModelScope)
    override val fabDropdown: IFabDropdownSubViewModel = _fabDropdown

    private val _toast = ToastSubViewModel(viewModelScope)
    override val toast: IToastSubViewModel = _toast

    override val editedTrackedThingName = MutableStateFlow(InputFieldData.EMPTY)
    override val editedTrackedThingSpellSlotLevel = MutableStateFlow(InputFieldData.EMPTY)
    override val editedTrackedThingValue = MutableStateFlow(InputFieldData.EMPTY)
    override val editedTrackedThingType = MutableStateFlow(TrackedThing.Type.None)
    override val confirmButtonEnabled = MutableStateFlow(false)

    private lateinit var allItems: List<Any>

    private var editedTrackedThing: TrackedThing? = null
    private var spellListBeingAddedTo: SpellList? = null
    private var spellBeingRemoved: SpellListEntry? = null

    override lateinit var dialogType: DialogType
    private val _spellListBeingPreviewed = MutableStateFlow<SpellList?>(null)
    override val spellListBeingPreviewed: StateFlow<SpellList?>
        get() = _spellListBeingPreviewed
    override var availableSpellSlotsForSpellBeingCast: List<Int>? = null

    override val combinedItemFlow: Flow<List<Any>> =
        _items.combine(_searchString) { items, searchString ->
            if (searchString.isBlank()) {
                items
            } else {
                (items + allItems)
                    .filter { item ->
                        item is INamed
                                && item.name.lowercase().contains(searchString.lowercase())
                    }
                    .sortedWith(SmartNamedSearchComparator(searchString))
            }
        }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                allItems = searchAllUseCase.getAllItems()
            }
        }
    }

    override fun getAllItems(): List<TrackedThing> {
        return trackedThingDao.getAllSortedByIndex(groupId)
            .onEach {
                if (it is SpellList) {
                    it.spells =
                        json.from<List<SpellListEntry>>(it.value, SpellList.spellListType)
                            .toMutableList()
                }
            }
    }

    override fun showCreateDialog(type: TrackedThing.Type) {
        viewModelScope.launch {
            _alertDialog._titleResource = type.nameResource
            dialogType = DialogType.Create
            editedTrackedThing = TrackedThing.emptyOfType(type, _items.value.size, groupId)
            editedTrackedThingName.emit(InputFieldData.EMPTY)
            editedTrackedThingSpellSlotLevel.emit(InputFieldData.EMPTY)
            editedTrackedThingValue.emit(InputFieldData.EMPTY)
            editedTrackedThingType.emit(type)
            validateModel()
            _fabDropdown.collapse()
            _alertDialog.show()
        }
    }

    override fun showEditDialog(item: TrackedThing) {
        viewModelScope.launch {
            _alertDialog._titleResource = R.string.edit
            dialogType = DialogType.Edit
            val copy = item.copy()
            editedTrackedThing = copy
            editedTrackedThingName.emit(InputFieldData(copy.name, isValid = true))
            if (copy is SpellSlot) {
                editedTrackedThingSpellSlotLevel.emit(
                    InputFieldData(
                        copy.level.toString(),
                        isValid = true
                    )
                )
            }
            editedTrackedThingValue.emit(InputFieldData(copy.defaultValue, isValid = true))
            editedTrackedThingType.emit(copy.type)
            validateModel()
            _fabDropdown.collapse()
            _alertDialog.show()
        }
    }

    override fun confirmDialogAction() {
        if (dialogType != DialogType.RefreshAll
            && dialogType != DialogType.ConfirmSpellRemovalFromList
            && editedTrackedThing?.validate() == false
        ) {
            return
        }

        when (dialogType) {
            DialogType.Create -> createNewTrackedThing()
            DialogType.Edit -> editExistingTrackedThing()
            DialogType.ConfirmDeletion -> deleteTrackedThing()

            DialogType.AddNumber,
            DialogType.ReduceNumber,
            DialogType.AddPercentage,
            DialogType.ReducePercentage ->
                changePercentageOrNumberOfTrackedThing()

            DialogType.HealHealth,
            DialogType.DamageHealth ->
                changeHealthOfTrackedThing()

            DialogType.AddTemporaryHp -> addTemporaryHpToTrackedThing()

            DialogType.ShowSpellList -> Unit
            DialogType.ConfirmSpellRemovalFromList -> removeSpellFromSpellList()
            DialogType.SelectSlotLevelToCastSpell -> Unit

            DialogType.EditText -> editText()

            DialogType.RefreshAll -> refreshAll()

            DialogType.None -> Unit
        }
    }

    private fun createNewTrackedThing() {
        viewModelScope.launch {
            val editedTrackedThing = requireNotNull(editedTrackedThing)
            withContext(Dispatchers.Default) {
                ensureNonEmptyName(editedTrackedThing)
                val id = trackedThingDao.insertOrUpdate(editedTrackedThing)
                editedTrackedThing.id = id
            }
            addItem(editedTrackedThing)
            _alertDialog.hide()
        }
    }

    private fun editExistingTrackedThing() {
        viewModelScope.launch {
            val trackedThingToUpdate = requireNotNull(editedTrackedThing)
            ensureNonEmptyName(trackedThingToUpdate)
            if (trackedThingToUpdate is Health) {
                trackedThingToUpdate.temporaryHp = 0
            }
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(trackedThingToUpdate)
            }
            replaceItem(trackedThingToUpdate)
            _alertDialog.hide()
        }
    }

    private fun ensureNonEmptyName(trackedThing: TrackedThing) {
        if (trackedThing.name.isBlank()) {
            trackedThing.name = groupName
        }
    }

    private fun deleteTrackedThing() {
        viewModelScope.launch {
            val editedTrackedThing = requireNotNull(editedTrackedThing)
            withContext(Dispatchers.Default) {
                trackedThingDao.delete(editedTrackedThing.id)
            }
            removeItem(editedTrackedThing)
            _alertDialog.hide()
        }
    }

    private fun changePercentageOrNumberOfTrackedThing() {
        viewModelScope.launch {
            val editedTrackedThing = requireNotNull(editedTrackedThing)
            when (dialogType) {
                DialogType.AddPercentage,
                DialogType.AddNumber ->
                    editedTrackedThing.add(editedTrackedThingValue.value.value)

                DialogType.ReducePercentage,
                DialogType.ReduceNumber ->
                    editedTrackedThing.subtract(editedTrackedThingValue.value.value)

                else -> throw Exception("$dialogType operation attempted on $editedTrackedThingType")
            }
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(editedTrackedThing)
            }
            _alertDialog.hide()
            replaceItem(editedTrackedThing)
        }
    }

    private fun changeHealthOfTrackedThing() {
        viewModelScope.launch {
            val editedTrackedThing = requireNotNull(editedTrackedThing)
            when (dialogType) {
                DialogType.HealHealth ->
                    editedTrackedThing.add(editedTrackedThingValue.value.value)

                DialogType.DamageHealth ->
                    editedTrackedThing.subtract(editedTrackedThingValue.value.value)

                else -> throw Exception("Changing health on non-health tracked thing.")
            }
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(editedTrackedThing)
            }
            _alertDialog.hide()
            replaceItem(editedTrackedThing)
        }
    }

    private fun addTemporaryHpToTrackedThing() {
        viewModelScope.launch {
            val editedTrackedThing = requireNotNull(editedTrackedThing)
            require(editedTrackedThing is Health)
            editedTrackedThing.addTemporaryHp(editedTrackedThingValue.value.value)
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(editedTrackedThing)
            }
            _alertDialog.hide()
            replaceItem(editedTrackedThing)
        }
    }

    private fun refreshAll() {
        viewModelScope.launch {
            _items.value
                .filterIsInstance<TrackedThing>()
                .forEach {
                    resetValueToDefault(it)
                }
            _alertDialog.hide()
        }
    }

    override fun resetValueToDefault(item: TrackedThing) {
        viewModelScope.launch {
            val itemCopy = item.copy()
            itemCopy.resetValueToDefault()
            if (itemCopy is Health) {
                itemCopy.temporaryHp = 0
            }
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(itemCopy)
            }
            replaceItem(itemCopy)
        }
    }

    override fun useAbility(item: TrackedThing) {
        reduceByOne(item)
    }

    override fun useSpell(item: TrackedThing) {
        reduceByOne(item)
    }

    private suspend fun useSpellSuspending(item: TrackedThing) {
        reduceByOneSuspending(item)
    }

    private fun reduceByOne(item: TrackedThing) {
        viewModelScope.launch {
            reduceByOneSuspending(item)
        }
    }

    private suspend fun reduceByOneSuspending(item: TrackedThing) {
        val itemCopy = item.copy()
        itemCopy.subtract("1")
        withContext(Dispatchers.Default) {
            trackedThingDao.insertOrUpdate(itemCopy)
        }
        replaceItem(itemCopy)
    }

    private fun addOne(item: TrackedThing) {
        viewModelScope.launch {
            val itemCopy = item.copy()
            itemCopy.add("1")
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(itemCopy)
            }
            replaceItem(itemCopy)
        }
    }

    override fun setName(name: String) {
        viewModelScope.launch {
            val editedTrackedThing = requireNotNull(editedTrackedThing)
            editedTrackedThing.name = name
            editedTrackedThingName.emit(
                InputFieldData(name, true)
            )
            validateModel()
        }
    }

    override fun setLevel(level: String) {
        viewModelScope.launch {
            val trackedThing = editedTrackedThing
            require(trackedThing is SpellSlot)
            trackedThing.level = level.toIntOrNull() ?: 0
            editedTrackedThingSpellSlotLevel.emit(
                InputFieldData(
                    level,
                    trackedThing.isLevelValid()
                )
            )
            validateModel()
        }
    }

    override fun setValue(value: String) {
        viewModelScope.launch {
            val editedTrackedThing = requireNotNull(editedTrackedThing)
            editedTrackedThing.setNewValue(value)
            editedTrackedThing.defaultValue = value
            editedTrackedThingValue.emit(
                InputFieldData(
                    value,
                    editedTrackedThing.isValueValid()
                )
            )
            validateModel()
        }
    }

    private fun validateModel() {
        viewModelScope.launch {
            confirmButtonEnabled.emit(editedTrackedThing?.validate() == true)
        }
    }

    override fun addToPercentageRequested(item: TrackedThing) =
        setupValueChangeDialog(
            item,
            DialogType.AddPercentage,
            R.string.increase_percentage_dialog_title
        )

    override fun subtractFromPercentageRequested(item: TrackedThing) =
        setupValueChangeDialog(
            item,
            DialogType.ReducePercentage,
            R.string.reduce_percentage_dialog_title
        )

    override fun addToNumberRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.AddNumber, R.string.add)

    override fun subtractFromNumberRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.ReduceNumber, R.string.subtract)

    override fun takeDamageRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.DamageHealth, R.string.take_damage_dialog_title)

    override fun healRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.HealHealth, R.string.heal_dialog_title)

    override fun addTemporaryHp(item: TrackedThing) =
        setupValueChangeDialog(
            item,
            DialogType.AddTemporaryHp,
            R.string.add_temporary_hp_dialog_title
        )

    private fun setupValueChangeDialog(
        item: TrackedThing,
        type: DialogType,
        @StringRes
        titleResource: Int
    ) {
        viewModelScope.launch {
            dialogType = type
            _alertDialog._titleResource = titleResource
            editedTrackedThing = item.copy()
            editedTrackedThingValue.emit(InputFieldData.EMPTY)
            validateModel()
            _alertDialog.show()
        }
    }

    override fun updateValueInputField(delta: String) {
        viewModelScope.launch {
            editedTrackedThingValue.emit(
                InputFieldData(
                    delta,
                    editedTrackedThing?.isValueValid() == true
                )
            )
        }
    }

    override fun deleteItemRequested(item: TrackedThing) {
        viewModelScope.launch {
            dialogType = DialogType.ConfirmDeletion
            _alertDialog._titleResource = R.string.delete_tracked_thing_dialog_title
            editedTrackedThing = item
            _alertDialog.show()
        }
    }

    override fun refreshAllRequested() {
        viewModelScope.launch {
            dialogType = DialogType.RefreshAll
            _alertDialog._titleResource = R.string.refresh_all_tracked_things_dialog_title
            _alertDialog.show()
        }
    }

    override fun itemReordered(from: Int, to: Int) {
        val itemCount = _items.value.size
        if (from == itemCount || to == itemCount) return

        viewModelScope.launch {
            val newList =
                _items
                    .value
                    .filterIsInstance<TrackedThing>()
                    .toMutableList()
                    .apply {
                        add(to, removeAt(from))
                        forEachIndexed { index, item ->
                            item.index = index
                        }
                    }
            _items.emit(newList)
            withContext(Dispatchers.Default) {
                newList.forEach {
                    trackedThingDao.insertOrUpdate(it)
                }
            }
        }
    }

    override fun showPreviewSpellListDialog(spellList: SpellList) {
        if (spellList.spells.isEmpty()) {
            return
        }
        viewModelScope.launch {
            _alertDialog._titleResource = R.string.spell_list
            dialogType = DialogType.ShowSpellList
            _spellListBeingPreviewed.emit(spellList)
            _alertDialog.show()
        }
    }

    override fun addSpellToList(spellId: Long) {
        viewModelScope.launch {
            val spellToAdd = withContext(Dispatchers.Default) {
                spellDao.getById(spellId)
            }
            val spellList = requireNotNull(spellListBeingAddedTo)
            val spellAlreadyInList =
                spellList.spells.any {
                    it.id == spellId && it.name == spellToAdd.name
                }
            if (spellAlreadyInList) {
                _toast.showMessage(R.string.spell_already_in_list)
            } else {
                spellList.spells.add(SpellListEntry.fromSpell(spellToAdd))
                val sortedSpells =
                    spellList.spells
                        .sortedWith { spell1, spell2 ->
                            requireNotNull(spell1)
                            requireNotNull(spell2)
                            val comparisonByLevel = spell1.level.compareTo(spell2.level)
                            when {
                                comparisonByLevel != 0 -> comparisonByLevel
                                else -> spell1.name.compareTo(spell2.name)
                            }
                        }.toMutableList()
                spellList.setSpells(sortedSpells, json)
                withContext(Dispatchers.Default) {
                    trackedThingDao.insertOrUpdate(spellList)
                }
                replaceItem(spellList.copy())
                _toast.showMessage(R.string.spell_successfully_added_to_list)
            }
            spellListBeingAddedTo = null
        }
    }

    override fun addingSpellToList(spellList: SpellList) {
        spellListBeingAddedTo = spellList
    }

    override fun removeSpellFromSpellListRequested(spell: SpellListEntry) {
        viewModelScope.launch {
            _alertDialog.hide()
            awaitFrame()
            spellBeingRemoved = spell
            dialogType = DialogType.ConfirmSpellRemovalFromList
            _alertDialog._titleResource = R.string.confirm_spell_removal_from_list_dialog_title
            _alertDialog.show()
        }
    }

    private fun dialogToRemoveOrCastSpellFromSpellListResolved() {
        viewModelScope.launch {
            spellListBeingPreviewed.value?.let {
                _alertDialog.hide()
                awaitFrame()
                showPreviewSpellListDialog(it)
            }
        }
    }

    private fun removeSpellFromSpellList() {
        viewModelScope.launch {
            val spellList = requireNotNull(spellListBeingPreviewed.value)
            val spellToRemove = requireNotNull(spellBeingRemoved)
            spellList.spells.remove(spellToRemove)
            spellList.setSpells(spellList.spells, json)
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(spellList)
            }
            spellBeingRemoved = null
            dialogToRemoveOrCastSpellFromSpellListResolved()
        }
    }

    override fun castSpellRequested(level: Int) {
        viewModelScope.launch {
            val availableSpellSlots =
                _items.value
                    .filterIsInstance<SpellSlot>()
                    .filter { it.level >= level && it.amount > 0 }
                    .map { it.level }
            require(availableSpellSlots.isNotEmpty())
            if (availableSpellSlots.size == 1) {
                castSpellImmediate(availableSpellSlots.first())
                return@launch
            }
            _alertDialog.hide()
            awaitFrame()
            availableSpellSlotsForSpellBeingCast = availableSpellSlots
            dialogType = DialogType.SelectSlotLevelToCastSpell
            _alertDialog._titleResource = R.string.select_slot_level_for_casting_spell
            _alertDialog.show()
        }
    }

    private fun castSpellImmediate(withSlotLevel: Int) {
        viewModelScope.launch {
            val spellSlot =
                _items.value
                    .filterIsInstance<SpellSlot>()
                    .first { it.level == withSlotLevel && it.amount > 0 }
            useSpellSuspending(spellSlot)
            _toast.showMessage(R.string.spell_cast_with_slot_level, withSlotLevel.toString())
            _spellListBeingPreviewed.emit(_spellListBeingPreviewed.value!!.copy() as SpellList)
        }
    }

    override fun castSpell(withSlotLevel: Int) {
        viewModelScope.launch {
            _alertDialog.hide()
            val spellSlot =
                _items.value
                    .filterIsInstance<SpellSlot>()
                    .first { it.level == withSlotLevel && it.amount > 0 }
            useSpellSuspending(spellSlot)
            availableSpellSlotsForSpellBeingCast = null
            _toast.showMessage(R.string.spell_cast_with_slot_level, withSlotLevel.toString())
            dialogToRemoveOrCastSpellFromSpellListResolved()
        }
    }

    override fun canCastSpell(level: Int): Boolean =
        _items.value
            .filterIsInstance<SpellSlot>()
            .any { it.level >= level && it.amount > 0 }

    override fun changeSpellListEntryPreparedState(
        spellListEntry: SpellListEntry,
        isPrepared: Boolean
    ) {
        viewModelScope.launch {
            val spellList = requireNotNull(spellListBeingPreviewed.value)
            spellListEntry.isPrepared = isPrepared
            spellList.setSpells(spellList.spells, json)
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(spellList)
            }
            val spellListCopy = spellList.copy() as SpellList
            replaceItem(spellListCopy)
            _spellListBeingPreviewed.emit(spellListCopy)
        }
    }

    private fun editText() {
        viewModelScope.launch {
            val editedTrackedThing = requireNotNull(editedTrackedThing)
            require(editedTrackedThing is Text)
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(editedTrackedThing)
            }
            _alertDialog.hide()
            replaceItem(editedTrackedThing)
        }

    }

    override fun useHitDie(item: TrackedThing) {
        reduceByOne(item)
    }

    override fun restoreHitDie(item: TrackedThing) {
        addOne(item)
    }

    private fun onAlertDialogDismissed() {
        when (dialogType) {
            DialogType.ShowSpellList -> {
                viewModelScope.launch {
                    _spellListBeingPreviewed.emit(null)
                }
            }

            DialogType.ConfirmSpellRemovalFromList -> {
                spellBeingRemoved = null
                dialogToRemoveOrCastSpellFromSpellListResolved()
            }

            DialogType.SelectSlotLevelToCastSpell -> {
                availableSpellSlotsForSpellBeingCast = null
                dialogToRemoveOrCastSpellFromSpellListResolved()
            }

            else -> Unit
        }
        dialogType = DialogType.None
    }

}