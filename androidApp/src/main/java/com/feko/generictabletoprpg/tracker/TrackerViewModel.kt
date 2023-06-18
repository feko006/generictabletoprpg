package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.Common
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingsUseCase
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingUseCase
import com.feko.generictabletoprpg.tracker.TrackedThing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackerViewModel(
    private val getAllTrackedThingsUseCase: GetAllTrackedThingsUseCase,
    private val insertOrUpdateTrackedThingsUseCase: InsertOrUpdateTrackedThingUseCase
) : OverviewViewModel<TrackedThing>() {
    val editedTrackedThingName = MutableStateFlow(Common.InputFieldData.EMPTY)
    val editedTrackedThingSpellSlotLevel = MutableStateFlow(Common.InputFieldData.EMPTY)
    val editedTrackedThingValue = MutableStateFlow(Common.InputFieldData.EMPTY)
    val editedTrackedThingType = MutableStateFlow(TrackedThing.Type.None)
    val confirmButtonEnabled = MutableStateFlow(false)

    private lateinit var editedTrackedThing: TrackedThing

    override fun getAllItems(): List<TrackedThing> = getAllTrackedThingsUseCase.getAll()

    fun showDialog(type: TrackedThing.Type) {
        viewModelScope.launch {
            dialogTitle = type.name
            editedTrackedThing = TrackedThing.emptyOfType(type)
            editedTrackedThingName.emit(Common.InputFieldData.EMPTY)
            editedTrackedThingSpellSlotLevel.emit(Common.InputFieldData.EMPTY)
            editedTrackedThingValue.emit(Common.InputFieldData.EMPTY)
            editedTrackedThingType.emit(type)
            validateModel()
            _isFabDropdownMenuExpanded.emit(false)
            _isDialogVisible.emit(true)
        }
    }

    fun validateAndCreateTrackedThing() {
        if (!editedTrackedThing.validate()) {
            return
        }
        if (editedTrackedThing.defaultValue.isBlank()) {
            editedTrackedThing.defaultValue = editedTrackedThing.value
        }
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val id = insertOrUpdateTrackedThingsUseCase.insertOrUpdate(editedTrackedThing)
                editedTrackedThing.id = id
            }
            val items = _items.value.toMutableList()
            items.add(editedTrackedThing)
            _items.emit(items.sortedBy(TrackedThing::name))
            _isDialogVisible.emit(false)
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
            require(trackedThing is TrackedThing.SpellSlot)
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
}