package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.Common
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.tracker.DeleteTrackedThingUseCase
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingsUseCase
import com.feko.generictabletoprpg.tracker.Health
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingUseCase
import com.feko.generictabletoprpg.tracker.SpellSlot
import com.feko.generictabletoprpg.tracker.TrackedThing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackerViewModel(
    private val groupId: Long,
    private val getAllTrackedThingsUseCase: GetAllTrackedThingsUseCase,
    private val insertOrUpdateTrackedThingsUseCase: InsertOrUpdateTrackedThingUseCase,
    private val deleteTrackedThingUseCase: DeleteTrackedThingUseCase
) : OverviewViewModel<TrackedThing>() {
    val editedTrackedThingName = MutableStateFlow(Common.InputFieldData.EMPTY)
    val editedTrackedThingSpellSlotLevel = MutableStateFlow(Common.InputFieldData.EMPTY)
    val editedTrackedThingValue = MutableStateFlow(Common.InputFieldData.EMPTY)
    val editedTrackedThingType = MutableStateFlow(TrackedThing.Type.None)
    val confirmButtonEnabled = MutableStateFlow(false)

    private lateinit var editedTrackedThing: TrackedThing
    lateinit var dialogType: DialogType

    override fun getAllItems(): List<TrackedThing> = getAllTrackedThingsUseCase.getAll(groupId)

    fun showCreateDialog(type: TrackedThing.Type) {
        viewModelScope.launch {
            dialogTitle = type.name
            dialogType = DialogType.Create
            editedTrackedThing = TrackedThing.emptyOfType(type, groupId)
            editedTrackedThingName.emit(Common.InputFieldData.EMPTY)
            editedTrackedThingSpellSlotLevel.emit(Common.InputFieldData.EMPTY)
            editedTrackedThingValue.emit(Common.InputFieldData.EMPTY)
            editedTrackedThingType.emit(type)
            validateModel()
            _isFabDropdownMenuExpanded.emit(false)
            _isDialogVisible.emit(true)
        }
    }

    fun showEditDialog(item: TrackedThing) {
        viewModelScope.launch {
            dialogTitle = "Edit"
            dialogType = DialogType.Edit
            val copy = item.copy()
            editedTrackedThing = copy
            editedTrackedThingName.emit(Common.InputFieldData(copy.name, isValid = true))
            if (copy is SpellSlot) {
                editedTrackedThingSpellSlotLevel.emit(
                    Common.InputFieldData(
                        copy.level.toString(),
                        isValid = true
                    )
                )
            }
            editedTrackedThingValue.emit(Common.InputFieldData(copy.defaultValue, isValid = true))
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
                val id = insertOrUpdateTrackedThingsUseCase.insertOrUpdate(editedTrackedThing)
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
                insertOrUpdateTrackedThingsUseCase.insertOrUpdate(trackedThingToUpdate)
            }
            replaceItem(trackedThingToUpdate)
            _isDialogVisible.emit(false)
        }
    }

    private fun deleteTrackedThing() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                deleteTrackedThingUseCase.delete(editedTrackedThing)
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
                insertOrUpdateTrackedThingsUseCase.insertOrUpdate(editedTrackedThing)
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
                insertOrUpdateTrackedThingsUseCase.insertOrUpdate(editedTrackedThing)
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
                insertOrUpdateTrackedThingsUseCase.insertOrUpdate(editedTrackedThing)
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
                insertOrUpdateTrackedThingsUseCase.insertOrUpdate(itemCopy)
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
                insertOrUpdateTrackedThingsUseCase.insertOrUpdate(itemCopy)
            }
            replaceItem(itemCopy)
        }
    }

    fun setName(name: String) {
        viewModelScope.launch {
            editedTrackedThing.name = name
            editedTrackedThingName.emit(
                Common.InputFieldData(
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
                Common.InputFieldData(
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
                Common.InputFieldData(
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
        setupValueChangeDialog(item, DialogType.AddPercentage, "Increase percentage")

    fun subtractFromPercentageRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.ReducePercentage, "Reduce percentage")

    fun addToNumberRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.AddNumber, "Add")

    fun subtractFromNumberRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.ReduceNumber, "Subtract")

    fun takeDamageRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.DamageHealth, "Take damage")

    fun healRequested(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.HealHealth, "Heal")

    fun addTemporaryHp(item: TrackedThing) =
        setupValueChangeDialog(item, DialogType.AddTemporaryHp, "Add temporary HP")

    private fun setupValueChangeDialog(
        item: TrackedThing,
        type: DialogType,
        title: String
    ) {
        viewModelScope.launch {
            dialogType = type
            dialogTitle = title
            editedTrackedThing = item.copy()
            editedTrackedThingValue.emit(Common.InputFieldData.EMPTY)
            validateModel()
            _isDialogVisible.emit(true)
        }
    }

    fun updateValueInputField(delta: String) {
        viewModelScope.launch {
            editedTrackedThingValue.emit(
                Common.InputFieldData(
                    delta,
                    editedTrackedThing.isValueValid()
                )
            )
        }
    }

    fun deleteItemRequested(item: TrackedThing) {
        viewModelScope.launch {
            dialogType = DialogType.ConfirmDeletion
            dialogTitle = "Delete?"
            editedTrackedThing = item
            _isDialogVisible.emit(true)
        }
    }

    fun refreshAllRequested() {
        viewModelScope.launch {
            dialogType = DialogType.RefreshAll
            dialogTitle = "Refresh all?"
            _isDialogVisible.emit(true)
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