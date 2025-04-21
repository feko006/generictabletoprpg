package com.feko.generictabletoprpg.tracker

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.INamed
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.common.SmartNamedSearchComparator
import com.feko.generictabletoprpg.common.alertdialog.AlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IStatefulAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.StatefulAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.composable.InputFieldData
import com.feko.generictabletoprpg.common.fabdropdown.FabDropdownSubViewModel
import com.feko.generictabletoprpg.common.fabdropdown.IFabDropdownSubViewModel
import com.feko.generictabletoprpg.common.toast.IToastSubViewModel
import com.feko.generictabletoprpg.common.toast.ToastSubViewModel
import com.feko.generictabletoprpg.import.IJson
import com.feko.generictabletoprpg.searchall.ISearchAllUseCase
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.tracker.dialogs.IAlertDialogTrackerViewModel.DialogType
import com.feko.generictabletoprpg.tracker.dialogs.StatsEditDialogSubViewModel
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

    override var statsEditDialog: StatsEditDialogSubViewModel =
        StatsEditDialogSubViewModel(
            viewModelScope,
            json,
            confirmButtonEnabled,
            alertDialog
        ) { confirmDialogAction() }

    private lateinit var allItems: List<Any>

    private var editedTrackedThing: TrackedThing? = null
    private var spellListBeingAddedTo: SpellList? = null
    private var fiveEDefaultStats: List<StatEntry>? = null

    override lateinit var dialogType: DialogType
    private val _spellListBeingPreviewed = MutableStateFlow<SpellList?>(null)
    override lateinit var spellListState: LazyListState
    override lateinit var isShowingPreparedSpells: MutableStateFlow<Boolean>
    override val spellListBeingPreviewed: StateFlow<SpellList?>
        get() = _spellListBeingPreviewed
    override var availableSpellSlotsForSpellBeingCast: List<Int>? = null
    override var statsBeingPreviewed: StatsContainer? = null

    private val _confirmDeletionDialog =
        StatefulAlertDialogSubViewModel<TrackedThing>(TrackedThing.Companion.Empty, viewModelScope)
    val confirmDeletionDialog: IStatefulAlertDialogSubViewModel<TrackedThing>
        get() = _confirmDeletionDialog

    private val _refreshAllDialog = AlertDialogSubViewModel(viewModelScope)
    val refreshAllDialog: IAlertDialogSubViewModel
        get() = _refreshAllDialog

    private val _confirmSpellRemovalFromListDialog =
        StatefulAlertDialogSubViewModel(
            SpellListEntry.Empty,
            viewModelScope,
            ::dialogToRemoveOrCastSpellFromSpellListResolved
        )
    val confirmSpellRemovalFromListDialog: IStatefulAlertDialogSubViewModel<SpellListEntry>
        get() = _confirmSpellRemovalFromListDialog

    private val _addPercentageDialog =
        StatefulAlertDialogSubViewModel(Percentage.Empty, viewModelScope)
    val addPercentageDialog: IStatefulAlertDialogSubViewModel<Percentage>
        get() = _addPercentageDialog

    private val _reducePercentageDialog =
        StatefulAlertDialogSubViewModel(Percentage.Empty, viewModelScope)
    val reducePercentageDialog: IStatefulAlertDialogSubViewModel<Percentage>
        get() = _reducePercentageDialog

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
                isShowingPreparedSpells = MutableStateFlow(false)
            }
        }
    }

    override fun getAllItems(): List<TrackedThing> {
        return trackedThingDao.getAllSortedByIndex(groupId)
            .onEach {
                if (it is JsonTrackedThing<*>) {
                    it.setItemFromValue(json)
                }
            }
    }

    override fun showCreateDialog(type: TrackedThing.Type, context: Context) {
        viewModelScope.launch {
            _alertDialog._titleResource = type.nameResource
            if (type == TrackedThing.Type.FiveEStats) {
                dialogType = DialogType.EditStats
                fiveEDefaultStats = fiveEDefaultStats ?: createDefault5EStatEntries(context)
                val defaultStats = requireNotNull(fiveEDefaultStats)
                val newStats = TrackedThing.emptyOfType(type, _items.value.size, groupId) as Stats
                val defaultStatsContainer =
                    newStats.serializedItem.copy(stats = defaultStats)
                newStats.setItem(defaultStatsContainer, json)
                statsEditDialog.editedStats.emit(newStats)
                confirmButtonEnabled.emit(true)
            } else {
                dialogType = DialogType.Create
                editedTrackedThing = TrackedThing.emptyOfType(type, _items.value.size, groupId)
                editedTrackedThingName.emit(InputFieldData.EMPTY)
                editedTrackedThingSpellSlotLevel.emit(InputFieldData.EMPTY)
                editedTrackedThingValue.emit(InputFieldData.EMPTY)
                validateModel()
            }
            editedTrackedThingType.emit(type)
            _fabDropdown.collapse()
            _alertDialog.show()
        }
    }

    override fun showEditDialog(item: TrackedThing) {
        viewModelScope.launch {
            _alertDialog._titleResource = R.string.edit
            val copy = item.copy()
            if (item.type == TrackedThing.Type.FiveEStats) {
                dialogType = DialogType.EditStats
                statsEditDialog.editedStats.emit(copy as Stats)
                confirmButtonEnabled.emit(true)
            } else {
                dialogType = DialogType.Edit
                editedTrackedThing = copy
                editedTrackedThingName.emit(InputFieldData(copy.name, isValid = true))
                if (copy is SpellSlot) {
                    val dereferenceTrackedThing = requireNotNull(editedTrackedThing)
                    dereferenceTrackedThing.setNewValue(dereferenceTrackedThing.defaultValue)
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
            }
            _fabDropdown.collapse()
            _alertDialog.show()
        }
    }

    override fun confirmDialogAction() {
        if (editedTrackedThing?.validate() == false) {
            return
        }

        when (dialogType) {
            DialogType.Create -> createNewTrackedThing()
            DialogType.Edit -> editExistingTrackedThing()

            DialogType.AddNumber,
            DialogType.ReduceNumber ->
                changePercentageOrNumberOfTrackedThing()

            DialogType.HealHealth,
            DialogType.DamageHealth ->
                changeHealthOfTrackedThing()

            DialogType.AddTemporaryHp -> addTemporaryHpToTrackedThing()

            DialogType.EditText -> editText()

            DialogType.EditStats -> createOrEditStats()


            DialogType.ShowSpellList,
            DialogType.SelectSlotLevelToCastSpell,
            DialogType.PreviewStatSkills,
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
            addItem(editedTrackedThing) {
                if (it is TrackedThing)
                    it.index
                else Int.MAX_VALUE
            }
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

    fun deleteTrackedThing(trackedThing: TrackedThing) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                trackedThingDao.delete(trackedThing.id)
            }
            removeItem(trackedThing)
            _alertDialog.hide()
        }
    }

    fun addToPercentage(percentage: Percentage, amount: String) {
        viewModelScope.launch {
            percentage.add(amount)
            withContext(Dispatchers.IO) {
                trackedThingDao.insertOrUpdate(percentage)
            }
            replaceItem(percentage.copy())
        }
    }

    fun subtractFromPercentage(percentage: Percentage, amount: String) {
        viewModelScope.launch {
            percentage.subtract(amount)
            withContext(Dispatchers.IO) {
                trackedThingDao.insertOrUpdate(percentage)
            }
            replaceItem(percentage.copy())
        }
    }

    private fun changePercentageOrNumberOfTrackedThing() {
        viewModelScope.launch {
            val editedTrackedThing = requireNotNull(editedTrackedThing)
            when (dialogType) {
                DialogType.AddNumber ->
                    editedTrackedThing.add(editedTrackedThingValue.value.value)

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

    fun refreshAll() {
        viewModelScope.launch {
            _items.value
                .filterIsInstance<TrackedThing>()
                .forEach {
                    resetValueToDefault(it)
                }
            _alertDialog.hide()
        }
    }

    private fun createOrEditStats() {
        val editedStats = statsEditDialog.editedStats.value
        viewModelScope.launch {
            val updatedStatsContainer =
                requireNotNull(editedStats)
                    .serializedItem
                    .let(::finalizeStats)
            editedStats.setItem(updatedStatsContainer, json)
            val isNewItem = editedStats.id <= 0
            withContext(Dispatchers.Default) {
                ensureNonEmptyName(editedStats)
                val id = trackedThingDao.insertOrUpdate(editedStats)
                editedStats.id = id
            }
            if (isNewItem) {
                addItem(editedStats) {
                    if (it is TrackedThing)
                        it.index
                    else Int.MAX_VALUE
                }
            } else {
                replaceItem(editedStats)
            }
            _alertDialog.hide()
        }
    }

    private fun finalizeStats(statsContainer: StatsContainer): StatsContainer {
        val stats =
            statsContainer.stats
                .map { stat ->
                    val skills = stat.skills.map { skill ->
                        val bonus =
                            getBonus(statsContainer.proficiencyBonus, stat, skill)
                        val passiveScore =
                            getPassiveScore(bonus, stat, skill)
                        skill.copy(
                            bonus = bonus,
                            passiveScore = passiveScore
                        )
                    }
                    val savingThrowBonus =
                        getSavingThrowBonus(statsContainer.proficiencyBonus, stat)
                    stat.copy(
                        savingThrowBonus = savingThrowBonus,
                        skills = skills
                    )
                }
        val initiative = getInitiative(statsContainer, stats)
        val spellSaveDc = getSpellSaveDc(statsContainer, stats)
        val spellAttackBonus = getSpellAttackBonus(statsContainer, stats)
        return statsContainer.copy(
            spellSaveDc = spellSaveDc,
            spellAttackBonus = spellAttackBonus,
            initiative = initiative,
            stats = stats
        )
    }

    private fun getInitiative(statsContainer: StatsContainer, stats: List<StatEntry>): Int {
        var initiative = statsContainer.initiative
        if (statsContainer.use5eCalculations) {
            val dexterityStat = stats.first { it.shortName.lowercase() == "dex" }
            initiative = dexterityStat.bonus +
                    statsContainer.initiativeAdditionalBonus
        }
        return initiative
    }

    private fun getSavingThrowBonus(proficiencyBonus: Int, stat: StatEntry): Int {
        var savingThrowBonus = stat.savingThrowBonus
        if (stat.use5ESkillBonusCalculation) {
            savingThrowBonus = stat.bonus +
                    stat.savingThrowAdditionalBonus +
                    if (stat.isProficientInSavingThrow) proficiencyBonus else 0
        }
        return savingThrowBonus
    }

    private fun getBonus(proficiencyBonus: Int, stat: StatEntry, skill: StatSkillEntry): Int {
        var bonus = skill.bonus
        if (stat.use5ESkillBonusCalculation) {
            bonus = stat.bonus +
                    skill.additionalBonus +
                    if (skill.isProficient) proficiencyBonus else 0
        }
        return bonus
    }

    private fun getPassiveScore(
        bonus: Int,
        stat: StatEntry,
        skill: StatSkillEntry
    ): Int {
        var passiveScore = skill.passiveScore
        if (stat.use5ESkillBonusCalculation) {
            passiveScore = 10 + bonus
        }
        return passiveScore
    }

    private fun getSpellSaveDc(
        statsContainer: StatsContainer,
        stats: List<StatEntry>
    ): Int {
        var spellSaveDc = statsContainer.spellSaveDc
        if (statsContainer.use5eCalculations) {
            val spellcastingModifierStat =
                stats.firstOrNull { stat -> stat.isSpellcastingModifier }
            spellSaveDc =
                8 + (spellcastingModifierStat?.bonus ?: 0) +
                        statsContainer.spellSaveDcAdditionalBonus +
                        statsContainer.proficiencyBonus
        }
        return spellSaveDc
    }

    private fun getSpellAttackBonus(statsContainer: StatsContainer, stats: List<StatEntry>): Int {
        var spellAttackBonus = statsContainer.spellAttackBonus
        if (statsContainer.use5eCalculations) {
            val spellcastingModifierStat =
                stats.firstOrNull { stat -> stat.isSpellcastingModifier }
            spellAttackBonus =
                (spellcastingModifierStat?.bonus ?: 0) +
                        statsContainer.spellAttackAdditionalBonus +
                        statsContainer.proficiencyBonus
        }
        return spellAttackBonus
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

    fun addToPercentageRequested(percentage: Percentage) =
        viewModelScope.launch { _addPercentageDialog.show(percentage) }

    fun subtractFromPercentageRequested(percentage: Percentage) =
        viewModelScope.launch { _reducePercentageDialog.show(percentage) }

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
        viewModelScope.launch { _confirmDeletionDialog.show(item) }
    }

    override fun refreshAllRequested() {
        viewModelScope.launch { _refreshAllDialog.show() }
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

    override fun showPreviewSpellListDialog(spellList: SpellList, resetListState: Boolean) {
        if (spellList.serializedItem.isEmpty()) {
            return
        }
        if (resetListState) {
            spellListState = LazyListState()
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
                spellList.serializedItem.any {
                    it.id == spellId && it.name == spellToAdd.name
                }
            if (spellAlreadyInList) {
                _toast.showMessage(R.string.spell_already_in_list)
            } else {
                spellList.serializedItem.add(SpellListEntry.fromSpell(spellToAdd))
                val sortedSpells =
                    spellList.serializedItem
                        .sortedWith { spell1, spell2 ->
                            requireNotNull(spell1)
                            requireNotNull(spell2)
                            val comparisonByLevel = spell1.level.compareTo(spell2.level)
                            when {
                                comparisonByLevel != 0 -> comparisonByLevel
                                else -> spell1.name.compareTo(spell2.name)
                            }
                        }.toMutableList()
                spellList.setItem(sortedSpells, json)
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
            _confirmSpellRemovalFromListDialog.show(spell)
        }
    }

    private fun dialogToRemoveOrCastSpellFromSpellListResolved() {
        viewModelScope.launch {
            spellListBeingPreviewed.value?.let {
                _alertDialog.hide()
                awaitFrame()
                showPreviewSpellListDialog(it, resetListState = false)
            }
        }
    }

    fun removeSpellFromSpellList(spellListEntry: SpellListEntry) {
        viewModelScope.launch {
            val spellList = requireNotNull(spellListBeingPreviewed.value)
            spellList.serializedItem.remove(spellListEntry)
            spellList.setItem(spellList.serializedItem, json)
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(spellList)
            }
            replaceItem(spellList.copy())
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

    override fun showStatsDialog(stats: Stats) {
        viewModelScope.launch {
            _alertDialog._titleResource = R.string.skills
            dialogType = DialogType.PreviewStatSkills
            statsBeingPreviewed = stats.serializedItem
            _alertDialog.show()
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
            spellList.setItem(spellList.serializedItem, json)
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(spellList)
            }
            val spellListCopy = spellList.copy() as SpellList
            replaceItem(spellListCopy)
            _spellListBeingPreviewed.emit(spellListCopy)
        }
    }

    override fun setShowingPreparedSpells(value: Boolean) {
        viewModelScope.launch {
            isShowingPreparedSpells.value = value
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

            DialogType.SelectSlotLevelToCastSpell -> {
                availableSpellSlotsForSpellBeingCast = null
                dialogToRemoveOrCastSpellFromSpellListResolved()
            }

            else -> Unit
        }
        dialogType = DialogType.None
    }

}