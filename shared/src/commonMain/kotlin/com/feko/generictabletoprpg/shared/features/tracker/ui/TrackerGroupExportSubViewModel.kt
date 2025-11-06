package com.feko.generictabletoprpg.shared.features.tracker.ui

import com.feko.generictabletoprpg.shared.common.data.json
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllByParentSortedByIndexDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.ExportState
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.ExportSubViewModel
import com.feko.generictabletoprpg.shared.features.io.domain.model.AppModel
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.writeString
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrackerGroupExportSubViewModel(
    private val getAllTrackedThingGroups: IGetAllDao<TrackedThingGroup>,
    private val getAllTrackedThings: IGetAllByParentSortedByIndexDao<TrackedThing>
) : ExportSubViewModel<TrackedThingGroup>() {

    override fun getExportedFileData(): String {
        val dereferencedState = exportState
        return when (dereferencedState) {
            is ExportState.ExportingAll -> {
                val date =
                    SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
                        .format(Date())
                "tracked-groups-$date"
            }

            is ExportState.ExportingSingle -> {
                val trackedGroupFileName = dereferencedState.item.name.replace(" ", "-")
                "tracked-group-$trackedGroupFileName"
            }

            else ->
                throw IllegalStateException("File data requested without previously specifying whether to export all or a single item.")
        }

    }

    @Suppress("MoveVariableDeclarationIntoWhen")
    override suspend fun exportDataInternal(file: PlatformFile) {
        val dereferencedState = exportState
        val json = when (dereferencedState) {
            is ExportState.ExportingAll -> {
                val allTrackedThingGroups = getAllTrackedThingGroups.getAllSortedByName().first()
                allTrackedThingGroups.forEach {
                    it.trackedThings = getAllTrackedThings.getAllSortedByIndex(it.id).first()
                }
                json.encodeToString(
                    AppModel.serializer(),
                    AppModel(trackedGroups = allTrackedThingGroups)
                )
            }

            is ExportState.ExportingSingle -> {
                val trackedThingGroup =
                    dereferencedState.item
                        .apply {
                            trackedThings = getAllTrackedThings.getAllSortedByIndex(id).first()
                        }
                json.encodeToString(
                    AppModel.serializer(),
                    AppModel(trackedGroups = listOf(trackedThingGroup))
                )
            }

            else ->
                throw IllegalStateException("File data requested without previously specifying whether to export all or a single item.")
        }
        file.writeString(json)
    }
}