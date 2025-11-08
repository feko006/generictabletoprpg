package com.feko.generictabletoprpg.shared.features.io.domain.usecase

import com.feko.generictabletoprpg.shared.common.data.json
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertOrUpdateDao
import com.feko.generictabletoprpg.shared.features.action.Action
import com.feko.generictabletoprpg.shared.features.condition.Condition
import com.feko.generictabletoprpg.shared.features.disease.Disease
import com.feko.generictabletoprpg.shared.features.io.domain.model.AppModel
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup
import com.feko.generictabletoprpg.shared.logger

//import timber.log.Timber

class JsonImportAllUseCase(
    private val insertActions: IInsertAllDao<Action>,
    private val insertConditions: IInsertAllDao<Condition>,
    private val insertDiseases: IInsertAllDao<Disease>,
    private val insertTrackedGroup: IInsertOrUpdateDao<TrackedThingGroup>,
    private val insertTrackedThings: IInsertAllDao<TrackedThing>
) : IJsonImportAllUseCase {
    override suspend fun import(content: String): Result<Boolean> {
        try {
            val appModel = json.decodeFromString(AppModel.serializer(), content)
            val results = mutableListOf<Result<Boolean>>()

            val actionsImported = importActions(appModel)
            results.add(actionsImported)

            val conditionsImported = importConditions(appModel)
            results.add(conditionsImported)

            val diseasesImported = importDiseases(appModel)
            results.add(diseasesImported)

            val trackedGroupsImported = importTrackedGroups(appModel.trackedGroups)
            results.add(trackedGroupsImported)

            return results.fold(Result.success(true)) { current, result ->
                val currentResult = current.getOrDefault(false)
                val nextResult = result.getOrDefault(false)
                Result.success(currentResult && nextResult)
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to process file" }
            return Result.failure(e)
        }
    }

    private suspend fun importTrackedGroups(trackedGroups: List<TrackedThingGroup>): Result<Boolean> {
        return if (trackedGroups.isEmpty()) {
            Result.success(true)
        } else {
            val results = mutableListOf<Result<Boolean>>()
            trackedGroups.forEach { trackedThingGroup ->
                val id = insertTrackedGroup.insertOrUpdate(trackedThingGroup)
                trackedThingGroup.trackedThings
                    .forEach { trackedThing ->
                        trackedThing.groupId = id
                    }
                val result = insertTrackedThings.insertAll(trackedThingGroup.trackedThings)
                results.add(result)
            }
            results.fold(Result.success(true)) { current, result ->
                val currentResult = current.getOrDefault(false)
                val nextResult = result.getOrDefault(false)
                Result.success(currentResult && nextResult)
            }
        }
    }

    private suspend fun importActions(appModel: AppModel): Result<Boolean> {
        val actions = appModel.sources.flatMap { source ->
            source.actions.map {
                it.source = source.name
                it
            }
        }
        return if (actions.isEmpty()) {
            Result.success(true)
        } else {
            insertActions.insertAll(actions)
        }
    }

    private suspend fun importConditions(appModel: AppModel): Result<Boolean> {
        val conditions = appModel.sources.flatMap { source ->
            source.conditions.map {
                it.source = source.name
                it
            }
        }
        return if (conditions.isEmpty()) {
            Result.success(true)
        } else {
            insertConditions.insertAll(conditions)
        }
    }

    private suspend fun importDiseases(appModel: AppModel): Result<Boolean> {
        val diseases = appModel.sources.flatMap { source ->
            source.diseases.map {
                it.source = source.name
                it
            }
        }
        return if (diseases.isEmpty()) {
            Result.success(true)
        } else {
            insertDiseases.insertAll(diseases)
        }
    }
}

