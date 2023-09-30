package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.InputFieldData
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.tracker.Health
import com.feko.generictabletoprpg.tracker.SpellSlot
import com.feko.generictabletoprpg.tracker.TrackedThing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackerViewModel(
    private val groupId: Long,
    private val trackedThingDao: TrackedThingDao
) : OverviewViewModel<TrackedThing>(trackedThingDao) {
    val editedTrackedThingName = MutableStateFlow(InputFieldData.EMPTY)
    val editedTrackedThingSpellSlotLevel = MutableStateFlow(InputFieldData.EMPTY)
    val editedTrackedThingValue = MutableStateFlow(InputFieldData.EMPTY)
    val editedTrackedThingType = MutableStateFlow(TrackedThing.Type.None)
    val confirmButtonEnabled = MutableStateFlow(false)

    private lateinit var editedTrackedThing: TrackedThing
    lateinit var dialogType: DialogType

    override fun getAllItems(): List<TrackedThing> = trackedThingDao.getAllSortedByIndex(groupId)

    fun showCreateDialog(type: TrackedThing.Type) {
        viewModelScope.launch {
            dialogTitleResource = type.nameResource
            dialogType = DialogType.Create
            editedTrackedThing = TrackedThing.emptyOfType(type, _items.value.size, groupId)
            editedTrackedThingName.emit(InputFieldData.EMPTY)
            editedTrackedThingSpellSlotLevel.emit(InputFieldData.EMPTY)
            editedTrackedThingValue.emit(InputFieldData.EMPTY)
            editedTrackedThingType.emit(type)
            validateModel()
            _isFabDropdownMenuExpanded.emit(false)
            _isDialogVisible.emit(true)
        }
    }

    fun showEditDialog(item: TrackedThing) {
        viewModelScope.launch {
            dialogTitleResource = R.string.edit
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
            _isFabDropdownMenuExpanded.emit(false)
            _isDialogVisible.emit(true)
        }
    }

    fun confirmDialogAction() {
        if (dialogType != DialogType.RefreshAll
            && !editedTrackedThing.validate()
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

            DialogType.RefreshAll -> refreshAll()
        }
    }

    private fun createNewTrackedThing() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val id = trackedThingDao.insertOrUpdate(editedTrackedThing)
                editedTrackedThing.id = id
            }
            addItem(editedTrackedThing)
            _isDialogVisible.emit(false)
        }
    }

    private fun editExistingTrackedThing() {
        viewModelScope.launch {
            val trackedThingToUpdate = editedTrackedThing
            if (trackedThingToUpdate is Health) {
                trackedThingToUpdate.temporaryHp = 0
            }
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(trackedThingToUpdate)
            }
            replaceItem(trackedThingToUpdate)
            _isDialogVisible.emit(false)
        }
    }

    private fun deleteTrackedThing() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                trackedThingDao.delete(editedTrackedThing.id)
            }
            removeItem(editedTrackedThing)
            _isDialogVisible.emit(false)
        }
    }

    private fun changePercentageOrNumberOfTrackedThing() {
        viewModelScope.launch {
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
            _isDialogVisible.emit(false)
            replaceItem(editedTrackedThing)
        }
    }

    private fun changeHealthOfTrackedThing() {
        viewModelScope.launch {
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
            _isDialogVisible.emit(false)
            replaceItem(editedTrackedThing)
        }
    }

    private fun addTemporaryHpToTrackedThing() {
        viewModelScope.launch {
            val health = editedTrackedThing
            require(health is Health)
            health.addTemporaryHp(editedTrackedThingValue.value.value)
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(editedTrackedThing)
            }
            _isDialogVisible.emit(false)
            replaceItem(editedTrackedThing)
        }
    }

    private fun refreshAll() {
        viewModelScope.launch {
            _items.value.forEach {
                resetValueToDefault(it)
            }
            _isDialogVisible.emit(false)
        }
    }

    fun resetValueToDefault(item: TrackedThing) {
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

    fun useAbility(item: TrackedThing) {
        reduceByOne(item)
    }

    fun useSpell(item: TrackedThing) {
        reduceByOne(item)
    }

    private fun reduceByOne(item: TrackedThing) {
        viewModelScope.launch {
            val itemCopy = item.copy()
            itemCopy.subtract("1")
            withContext(Dispatchers.Default) {
                trackedThingDao.insertOrUpdate(itemCopy)
            }
            replaceItem(itemCopy)
        }
    }

    fun setName(name: String) {
        viewModelScope.launch {
            editedTrackedThing.name = name
            editedTrackedThingName.emit(
                InputFieldData(
                    name,
                    editedTrackedThing.isNameValid()
                )
            )
            validateModel()
        }
    }

    fun setLevel(level: String) {
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

    fun setValue(value: String) {
        viewModelScope.launch {
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
            confirmButtonEnabled.emit(editedTrackedThing.validate())
        }
    }

    fun addToPercentageRequested(item: TrackedThing) =
        setupValueChangeDialog(
            item,
            DialogType.AddPercentage,
            R.string.increase_percentage_dialog_title
        )

    fun subtractFromPercentageRequested(item: TrackedThing) =
        setupValueChangeDialog(
            item,
            DialogType.ReducePercentage,
            R.string.reduce_percentage_dialog_title
        )

    fun addToNumberRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.AddNumber, R.string.add)

    fun subtractFromNumberRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.ReduceNumber, R.string.subtract)

    fun takeDamageRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.DamageHealth, R.string.take_damage_dialog_title)

    fun healRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.HealHealth, R.string.heal_dialog_title)

    fun addTemporaryHp(item: TrackedThing) =
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
            dialogTitleResource = titleResource
            editedTrackedThing = item.copy()
            editedTrackedThingValue.emit(InputFieldData.EMPTY)
            validateModel()
            _isDialogVisible.emit(true)
        }
    }

    fun updateValueInputField(delta: String) {
        viewModelScope.launch {
            editedTrackedThingValue.emit(
                InputFieldData(
                    delta,
                    editedTrackedThing.isValueValid()
                )
            )
        }
    }

    fun deleteItemRequested(item: TrackedThing) {
        viewModelScope.launch {
            dialogType = DialogType.ConfirmDeletion
            dialogTitleResource = R.string.delete_tracked_thing_dialog_title
            editedTrackedThing = item
            _isDialogVisible.emit(true)
        }
    }

    fun refreshAllRequested() {
        viewModelScope.launch {
            dialogType = DialogType.RefreshAll
            dialogTitleResource = R.string.refresh_all_tracked_things_dialog_title
            _isDialogVisible.emit(true)
        }
    }

    fun itemReordered(from: Int, to: Int) {
        viewModelScope.launch {
            val newList =
                _items
                    .value
                    .toMutableList()
                    .apply {
                        add(from, removeAt(to))
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

    enum class DialogType {
        Create,
        Edit,
        ConfirmDeletion,
        AddPercentage,
        ReducePercentage,
        DamageHealth,
        HealHealth,
        AddTemporaryHp,
        RefreshAll,
        AddNumber,
        ReduceNumber
    }
}