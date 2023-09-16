package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.InputFieldData
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.tracker.TrackedThingGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackerGroupViewModel(
    private val trackedThingGroupDao: TrackedThingGroupDao
) : OverviewViewModel<TrackedThingGroup>(trackedThingGroupDao) {
    val groupName = MutableStateFlow(InputFieldData.EMPTY)
    val confirmButtonEnabled = MutableStateFlow(false)

    lateinit var dialogType: DialogType

    private var groupId: Long = 0L

    fun newTrackedThingGroupRequested() {
        viewModelScope.launch {
            dialogType = DialogType.NewOrUpdate
            dialogTitleResource = R.string.create_new_tracked_group_dialog_title
            groupId = 0
            groupName.emit(InputFieldData.EMPTY)
            confirmButtonEnabled.emit(false)
            _isDialogVisible.emit(true)
        }
    }

    fun editItemRequested(item: TrackedThingGroup) {
        viewModelScope.launch {
            dialogType = DialogType.NewOrUpdate
            dialogTitleResource = R.string.edit_tracked_group_dialog_title
            groupId = item.id
            groupName.emit(InputFieldData(item.name, true))
            confirmButtonEnabled.emit(true)
            _isDialogVisible.emit(true)
        }
    }

    fun deleteItemRequested(item: TrackedThingGroup) {
        viewModelScope.launch {
            dialogType = DialogType.Delete
            dialogTitleResource = R.string.delete_tracked_group_dialog_title
            groupId = item.id
            confirmButtonEnabled.emit(true)
            _isDialogVisible.emit(true)
        }
    }

    fun setName(name: String) {
        viewModelScope.launch {
            val isNewNameValid = name.isNotBlank()
            groupName.emit(
                InputFieldData(
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
            val trackedThingId = trackedThingGroupDao.insertOrUpdate(item)
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
            trackedThingGroupDao.delete(item.id)
        }
        removeItem(item)
    }

    enum class DialogType {
        NewOrUpdate,
        Delete
    }
}