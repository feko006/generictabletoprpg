package com.feko.generictabletoprpg.tracker

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.common.alertdialog.AlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.composable.InputFieldData
import com.feko.generictabletoprpg.export.IExportSubViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackerGroupViewModel(
    private val trackedThingGroupDao: TrackedThingGroupDao,
    val export: IExportSubViewModel<TrackedThingGroup>
) : OverviewViewModel<TrackedThingGroup>(trackedThingGroupDao) {

    private val _alertDialog = AlertDialogSubViewModel(viewModelScope)
    val alertDialog: IAlertDialogSubViewModel = _alertDialog
    val exportButtonVisible: Flow<Boolean> =
        _items.map { it.any() }
    val groupName = MutableStateFlow(InputFieldData.EMPTY)
    val confirmButtonEnabled = MutableStateFlow(false)

    lateinit var dialogType: DialogType

    private var groupId: Long = 0L

    fun newTrackedThingGroupRequested() {
        viewModelScope.launch {
            dialogType = DialogType.NewOrUpdate
            _alertDialog._titleResource = R.string.create_new_tracked_group_dialog_title
            groupId = 0
            groupName.emit(InputFieldData.EMPTY)
            confirmButtonEnabled.emit(false)
            _alertDialog.show()
        }
    }

    fun editItemRequested(item: TrackedThingGroup) {
        viewModelScope.launch {
            dialogType = DialogType.NewOrUpdate
            _alertDialog._titleResource = R.string.edit_tracked_group_dialog_title
            groupId = item.id
            groupName.emit(InputFieldData(item.name, true))
            confirmButtonEnabled.emit(true)
            _alertDialog.show()
        }
    }

    fun deleteItemRequested(item: TrackedThingGroup) {
        viewModelScope.launch {
            dialogType = DialogType.Delete
            _alertDialog._titleResource = R.string.delete_tracked_group_dialog_title
            groupId = item.id
            confirmButtonEnabled.emit(true)
            _alertDialog.show()
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
            _alertDialog.hide()
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
            addItem(item) { it.name }
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