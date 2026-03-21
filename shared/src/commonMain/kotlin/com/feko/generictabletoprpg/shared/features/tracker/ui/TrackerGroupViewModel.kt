package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.create_new_tracked_group_dialog_title
import com.feko.generictabletoprpg.delete_tracked_group_dialog_title
import com.feko.generictabletoprpg.edit_tracked_group_dialog_title
import com.feko.generictabletoprpg.export_failed
import com.feko.generictabletoprpg.export_location_hint
import com.feko.generictabletoprpg.export_successful
import com.feko.generictabletoprpg.shared.common.data.json
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.ToastMessage
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.ExportState
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.IExportViewModel
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.OverviewViewModel
import com.feko.generictabletoprpg.shared.features.io.domain.model.AppModel
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingDao
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingGroupDao
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup
import com.feko.generictabletoprpg.shared.logger
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.compose.SaverResultLauncher
import io.github.vinceglb.filekit.writeString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrackerGroupViewModel(
    private val trackedThingGroupDao: TrackedThingGroupDao,
    private val trackedThingDao: TrackedThingDao
) : OverviewViewModel<TrackedThingGroup>(trackedThingGroupDao),
    IExportViewModel<TrackedThingGroup> {

    private var _exportState: ExportState<TrackedThingGroup> = ExportState.None
    private val _dialog = MutableStateFlow<ITrackerGroupDialog>(ITrackerGroupDialog.None)
    val dialog: Flow<ITrackerGroupDialog> = _dialog

    val exportButtonVisible: Flow<Boolean> =
        _items.map { it.any() }

    private val _exportToast = MutableStateFlow<ToastMessage?>(null)
    val exportToast: Flow<ToastMessage?> = _exportToast

    fun newTrackedThingGroupRequested() {
        _dialog.update {
            ITrackerGroupDialog.EditDialog(
                TrackedThingGroup.Empty,
                IText.StringResourceText(Res.string.create_new_tracked_group_dialog_title)
            )
        }
    }

    fun editItemRequested(item: TrackedThingGroup) {
        _dialog.update {
            ITrackerGroupDialog.EditDialog(
                item,
                IText.StringResourceText(Res.string.edit_tracked_group_dialog_title)
            )
        }
    }

    fun deleteItemRequested(item: TrackedThingGroup) {
        _dialog.update {
            ITrackerGroupDialog.DeleteDialog(
                item,
                IText.StringResourceText(Res.string.delete_tracked_group_dialog_title)
            )
        }
    }

    fun insertOrUpdateGroup(item: TrackedThingGroup) {
        viewModelScope.launch {
            val isNewEntry = item.id == 0L
            if (isNewEntry) {
                val trackedThingId = trackedThingGroupDao.insert(item.toEntity())
                item.id = trackedThingId
                scrollToEnd()
            } else {
                trackedThingGroupDao.update(item.toEntity())
            }
        }
    }

    fun deleteGroup(item: TrackedThingGroup) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                trackedThingGroupDao.delete(item.id)
            }
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

    override fun export(item: TrackedThingGroup, fileSaverLauncher: SaverResultLauncher?) {
        _exportState = ExportState.ExportingSingle(item)
        _exportToast.update { ToastMessage(Res.string.export_location_hint.asText(), _exportToast) }
        val trackedGroupFileName = item.name.replace(" ", "-")
        val displayName = "tracked-group-$trackedGroupFileName"
        fileSaverLauncher?.launch(suggestedName = displayName, extension = "json")
    }

    override fun exportAll(fileSaverLauncher: SaverResultLauncher?) {
        _exportState = ExportState.ExportingAll
        _exportToast.update { ToastMessage(Res.string.export_location_hint.asText(), _exportToast) }
        val date = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
            .format(Date())
        val displayName = "tracked-groups-$date"
        fileSaverLauncher?.launch(suggestedName = displayName, extension = "json")
    }

    override suspend fun onFileSaveLocationSelected(file: PlatformFile?) {
        if (file == null) {
            _exportState = ExportState.None
            return
        }
        withContext(viewModelScope.coroutineContext) {
            try {
                val json = when (val exportState = _exportState) {
                    ExportState.ExportingAll -> getAllTrackedGroupsExportJson()
                    is ExportState.ExportingSingle<TrackedThingGroup> ->
                        getTrackedThingExportJson(exportState.item)

                    else -> throw Exception()
                }
                file.writeString(json)
                logger.debug { "Export successful." }
                _exportToast.emit(
                    ToastMessage(Res.string.export_successful.asText(), _exportToast)
                )
            } catch (e: Exception) {
                logger.error(e) { "Export failed." }
                _exportToast.emit(
                    ToastMessage(Res.string.export_failed.asText(), _exportToast)
                )
            } finally {
                _exportState = ExportState.None
            }
        }
    }

    private suspend fun getAllTrackedGroupsExportJson(): String {
        val allTrackedThingGroups = trackedThingGroupDao.getAllSortedByName().first()
        allTrackedThingGroups.forEach {
            it.trackedThings = trackedThingDao.getAllSortedByIndex(it.id).first()
        }
        return json.encodeToString(
            AppModel.serializer(), AppModel(trackedGroups = allTrackedThingGroups)
        )
    }

    private suspend fun getTrackedThingExportJson(item: TrackedThingGroup): String {
        item.trackedThings = trackedThingDao.getAllSortedByIndex(item.id).first()
        return json.encodeToString(
            AppModel.serializer(), AppModel(trackedGroups = listOf(item))
        )
    }
}