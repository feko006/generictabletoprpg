package com.feko.generictabletoprpg.features.tracker.ui

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IText
import com.feko.generictabletoprpg.common.ui.viewmodel.OverviewViewModel
import com.feko.generictabletoprpg.common.ui.viewmodel.IExportSubViewModel
import com.feko.generictabletoprpg.features.tracker.TrackedThingGroupDao
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThingGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackerGroupViewModel(
    private val trackedThingGroupDao: TrackedThingGroupDao,
    val export: IExportSubViewModel<TrackedThingGroup>
) : OverviewViewModel<TrackedThingGroup>(trackedThingGroupDao) {

    private val _dialog = MutableStateFlow<ITrackerGroupDialog>(ITrackerGroupDialog.None)
    val dialog: Flow<ITrackerGroupDialog> = _dialog

    val exportButtonVisible: Flow<Boolean> =
        _items.map { it.any() }

    fun newTrackedThingGroupRequested() {
        _dialog.update {
            ITrackerGroupDialog.EditDialog(
                TrackedThingGroup.Empty,
                IText.StringResourceText(R.string.create_new_tracked_group_dialog_title)
            )
        }
    }

    fun editItemRequested(item: TrackedThingGroup) {
        _dialog.update {
            ITrackerGroupDialog.EditDialog(
                item,
                IText.StringResourceText(R.string.edit_tracked_group_dialog_title)
            )
        }
    }

    fun deleteItemRequested(item: TrackedThingGroup) {
        _dialog.update {
            ITrackerGroupDialog.DeleteDialog(
                item,
                IText.StringResourceText(R.string.delete_tracked_group_dialog_title)
            )
        }
    }

    fun insertOrUpdateGroup(item: TrackedThingGroup) {
        viewModelScope.launch {
            val isNewEntry = item.id == 0L
            if (isNewEntry) {
                withContext(Dispatchers.IO) {
                    val trackedThingId = trackedThingGroupDao.insert(item.toEntity())
                    item.id = trackedThingId
                }
                addItem(item) { it.name }
            } else {
                withContext(Dispatchers.IO) {
                    trackedThingGroupDao.update(item.toEntity())
                }
                replaceItem(item)
            }
        }
    }

    fun deleteGroup(item: TrackedThingGroup) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                trackedThingGroupDao.delete(item.id)
            }
            removeItem(item)
        }
    }

    fun editDialogValueUpdated(trackedThingGroup: TrackedThingGroup) {
        _dialog.update {
            if (it !is ITrackerGroupDialog.EditDialog) return
            it.copy(trackedThingGroup = trackedThingGroup)
        }
    }

    fun dismissDialog() {
        _dialog.update { ITrackerGroupDialog.None }
    }
}