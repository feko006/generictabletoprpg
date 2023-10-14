package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.app.AppModel
import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.IGetAllByParentSortedByIndex
import com.feko.generictabletoprpg.export.ExportState
import com.feko.generictabletoprpg.export.ExportViewModelExtension
import com.feko.generictabletoprpg.import.IJson
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date

class TrackerGroupExportViewModelExtension(
    private val getAllTrackedThingGroups: IGetAll<TrackedThingGroup>,
    private val getAllTrackedThings: IGetAllByParentSortedByIndex<TrackedThing>,
    private val json: IJson
) : ExportViewModelExtension<TrackedThingGroup>() {

    override fun getExportedFileData(): Pair<String, String> {
        val dereferencedState = exportState
        val mimeType = "text/json"
        return when (dereferencedState) {
            is ExportState.ExportingAll -> {
                val date =
                    SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", java.util.Locale.getDefault())
                        .format(Date())
                Pair(mimeType, "tracked-groups-$date.json")
            }

            is ExportState.ExportingSingle -> {
                val trackedGroupFileName = dereferencedState.item.name.replace(" ", "-")
                Pair(
                    mimeType,
                    "tracked-group-$trackedGroupFileName.json"
                )
            }

            else ->
                throw IllegalStateException("File data requested without previously specifying whether to export all or a single item.")
        }

    }

    @Suppress("MoveVariableDeclarationIntoWhen")
    override suspend fun exportDataInternal(outputStream: OutputStream) {
        val dereferencedState = exportState
        val json = when (dereferencedState) {
            is ExportState.ExportingAll -> {
                val allTrackedThingGroups = getAllTrackedThingGroups.getAllSortedByName()
                allTrackedThingGroups.forEach {
                    it.trackedThings = getAllTrackedThings.getAllSortedByIndex(it.id)
                }
                json.to(
                    AppModel(trackedGroups = allTrackedThingGroups),
                    AppModel::class.java
                )
            }

            is ExportState.ExportingSingle -> {
                val trackedThingGroup =
                    dereferencedState.item
                        .apply {
                            trackedThings = getAllTrackedThings.getAllSortedByIndex(id)
                        }
                json.to(
                    AppModel(trackedGroups = listOf(trackedThingGroup)),
                    AppModel::class.java
                )
            }

            else ->
                throw IllegalStateException("File data requested without previously specifying whether to export all or a single item.")
        }
        outputStream
            .bufferedWriter()
            .use {
                it.write(json)
            }
    }
}