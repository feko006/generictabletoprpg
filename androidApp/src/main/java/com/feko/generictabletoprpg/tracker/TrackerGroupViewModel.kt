package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.Common
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.tracker.DeleteTrackedThingGroupUseCase
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingGroupsUseCase
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingGroupUseCase
import com.feko.generictabletoprpg.tracker.TrackedThingGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackerGroupViewModel(
    private val getAllTrackedThingGroupsUseCase: GetAllTrackedThingGroupsUseCase,
    private val insertOrUpdateTrackedThingGroupUseCase: InsertOrUpdateTrackedThingGroupUseCase,
    private val deleteTrackedThingGroupUseCase: DeleteTrackedThingGroupUseCase
) : OverviewViewModel<TrackedThingGroup>() {
    val groupName = MutableStateFlow(Common.InputFieldData.EMPTY)
    val confirmButtonEnabled = MutableStateFlow(false)

    lateinit var dialogType: DialogType

    private var groupId: Long = 0L

    override fun getAllItems(): List<TrackedThingGroup> = getAllTrackedThingGroupsUseCase.getAll()

    fun newTrackedThingGroupRequested() {
        viewModelScope.launch {
            dialogType = DialogType.NewOrUpdate
            dialogTitle = "Create new group"
            groupId = 0
            groupName.emit(Common.InputFieldData.EMPTY)
            confirmButtonEnabled.emit(false)
            _isDialogVisible.emit(true)
        }
    }

    fun editItemRequested(item: TrackedThingGroup) {
        viewModelScope.launch {
            dialogType = DialogType.NewOrUpdate
            dialogTitle = "Edit group"
            groupId = item.id
            groupName.emit(Common.InputFieldData(item.name, true))
            confirmButtonEnabled.emit(true)
            _isDialogVisible.emit(true)
        }
    }

    fun deleteItemRequested(item: TrackedThingGroup) {
        viewModelScope.launch {
            dialogType = DialogType.Delete
            dialogTitle = "Delete group"
            groupId = item.id
            confirmButtonEnabled.emit(true)
            _isDialogVisible.emit(true)
        }
    }

    fun setName(name: String) {
        viewModelScope.launch {
            val isNewNameValid = name.isNotBlank()
            groupName.emit(
                Common.InputFieldData(
                    name,
                    isNewNameValid
                )
            )
            confirmButtonEnabled.emit(isNewNameValid)
        }
    }

    fun confirmDialogAction() {
        viewModelScope.launch {
            when (dialogType) {
                DialogType.NewOrUpdate -> insertOrUpdateGroup()
                DialogType.Delete -> deleteGroup()
            }
            _isDialogVisible.emit(false)
        }
    }

    private suspend fun insertOrUpdateGroup() {
        val item = TrackedThingGroup(
            groupId,
            groupName.value.value
        )
        withContext(Dispatchers.Default) {
            val trackedThingId =
                insertOrUpdateTrackedThingGroupUseCase.insertOrUpdate(item)
            item.id = trackedThingId
        }
        if (groupId == 0L) {
            addItem(item)
        } else {
            replaceItem(item)
        }
    }

    private suspend fun deleteGroup() {
        val item = TrackedThingGroup(groupId, "")
        withContext(Dispatchers.Default) {
            deleteTrackedThingGroupUseCase.delete(item)
        }
        removeItem(item)
    }

    enum class DialogType {
        NewOrUpdate,
        Delete
    }
}