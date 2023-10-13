package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.action.Action
import com.feko.generictabletoprpg.app.AppModel
import com.feko.generictabletoprpg.common.IInsertAll
import com.feko.generictabletoprpg.common.IInsertOrUpdate
import com.feko.generictabletoprpg.condition.Condition
import com.feko.generictabletoprpg.disease.Disease
import com.feko.generictabletoprpg.tracker.TrackedThing
import com.feko.generictabletoprpg.tracker.TrackedThingGroup
import timber.log.Timber

class JsonImportAllUseCaseImpl(
    private val json: IJson,
    private val insertActions: IInsertAll<Action>,
    private val insertConditions: IInsertAll<Condition>,
    private val insertDiseases: IInsertAll<Disease>,
    private val insertTrackedGroup: IInsertOrUpdate<TrackedThingGroup>,
    private val insertTrackedThings: IInsertAll<TrackedThing>
) : JsonImportAllUseCase {
    override fun import(content: String): Result<Boolean> {
        try {
            val appModel = json.from(content, AppModel::class.java)
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
            Timber.e(e, "Failed to process file")
            return Result.failure(e)
        }
    }

    private fun importTrackedGroups(trackedGroups: List<TrackedThingGroup>): Result<Boolean> {
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

    private fun importActions(appModel: AppModel): Result<Boolean> {
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

    private fun importConditions(appModel: AppModel): Result<Boolean> {
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

    private fun importDiseases(appModel: AppModel): Result<Boolean> {
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

