package com.feko.generictabletoprpg.shared.features.io.domain.usecase

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.action
import com.feko.generictabletoprpg.ammunition
import com.feko.generictabletoprpg.armor
import com.feko.generictabletoprpg.condition
import com.feko.generictabletoprpg.disease
import com.feko.generictabletoprpg.feat
import com.feko.generictabletoprpg.magic_item
import com.feko.generictabletoprpg.shared.common.data.json
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertOrUpdateDao
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.features.action.Action
import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition
import com.feko.generictabletoprpg.shared.features.armor.Armor
import com.feko.generictabletoprpg.shared.features.condition.Condition
import com.feko.generictabletoprpg.shared.features.disease.Disease
import com.feko.generictabletoprpg.shared.features.feat.Feat
import com.feko.generictabletoprpg.shared.features.io.domain.model.AppModel
import com.feko.generictabletoprpg.shared.features.magicitem.MagicItem
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import com.feko.generictabletoprpg.shared.logger
import com.feko.generictabletoprpg.spell
import com.feko.generictabletoprpg.tracker_title
import com.feko.generictabletoprpg.weapon

class JsonImportAllUseCase(
    private val insertActions: IInsertAllDao<Action>,
    private val insertAmmunition: IInsertAllDao<Ammunition>,
    private val insertArmor: IInsertAllDao<Armor>,
    private val insertConditions: IInsertAllDao<Condition>,
    private val insertDiseases: IInsertAllDao<Disease>,
    private val insertFeats: IInsertAllDao<Feat>,
    private val insertSpells: IInsertAllDao<Spell>,
    private val insertWeapons: IInsertAllDao<Weapon>,
    private val insertMagicItems: IInsertAllDao<MagicItem>,
    private val insertTrackedGroup: IInsertOrUpdateDao<TrackedThingGroup>,
    private val insertTrackedThings: IInsertAllDao<TrackedThing>
) : IJsonImportAllUseCase {
    override suspend fun import(content: String): Result<List<ImportResult>> {
        try {
            val appModel = json.decodeFromString(AppModel.serializer(), content)
            val results = mutableListOf<ImportResult>()

            val actionsImported = importActions(appModel)
            actionsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.action.asText(), it.first, it.second))
            }

            val ammunitionImported = importAmmunition(appModel)
            ammunitionImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.ammunition.asText(), it.first, it.second))
            }

            val armorImported = importArmor(appModel)
            armorImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.armor.asText(), it.first, it.second))
            }

            val conditionsImported = importConditions(appModel)
            conditionsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.condition.asText(), it.first, it.second))
            }

            val diseasesImported = importDiseases(appModel)
            diseasesImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.disease.asText(), it.first, it.second))
            }

            val featsImported = importFeats(appModel)
            featsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.feat.asText(), it.first, it.second))
            }

            val spellsImported = importSpells(appModel)
            spellsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.spell.asText(), it.first, it.second))
            }

            val weaponsImported = importWeapons(appModel)
            weaponsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.weapon.asText(), it.first, it.second))
            }

            val magicItemsImported = importMagicItems(appModel)
            magicItemsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.magic_item.asText(), it.first, it.second))
            }

            val trackedGroupsImported = importTrackedGroups(appModel.trackedGroups)
            trackedGroupsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.tracker_title.asText(), it.first, it.second))
            }

            return Result.success(results)
        } catch (e: Exception) {
            logger.error(e) { "Failed to process file" }
            return Result.failure(e)
        }
    }

    private suspend fun importTrackedGroups(
        trackedGroups: List<TrackedThingGroup>
    ): Result<Pair<Int, Int>> {
        var successes = 0
        trackedGroups.forEach { trackedThingGroup ->
            val id = insertTrackedGroup.insertOrUpdate(trackedThingGroup)
            trackedThingGroup.trackedThings
                .forEach { trackedThing ->
                    trackedThing.groupId = id
                }
            val result = insertTrackedThings.insertAll(trackedThingGroup.trackedThings)
            if (result.isSuccess && result.getOrThrow().run { first == second }) {
                successes++
            }
        }
        return Result.success(Pair(successes, trackedGroups.size))
    }

    private suspend fun importActions(appModel: AppModel): Result<Pair<Int, Int>> {
        val actions = appModel.sources.flatMap { source ->
            source.actions.map {
                it.source = source.name
                it
            }
        }
        return insertActions.insertAll(actions)
    }

    private suspend fun importAmmunition(appModel: AppModel): Result<Pair<Int, Int>> {
        val ammunition = appModel.sources.flatMap { source ->
            source.ammunition.map {
                it.source = source.name
                it
            }
        }
        return insertAmmunition.insertAll(ammunition)
    }

    private suspend fun importArmor(appModel: AppModel): Result<Pair<Int, Int>> {
        val armor = appModel.sources.flatMap { source ->
            source.armor.map {
                it.source = source.name
                it
            }
        }
        return insertArmor.insertAll(armor)
    }

    private suspend fun importConditions(appModel: AppModel): Result<Pair<Int, Int>> {
        val conditions = appModel.sources.flatMap { source ->
            source.conditions.map {
                it.source = source.name
                it
            }
        }
        return insertConditions.insertAll(conditions)
    }

    private suspend fun importDiseases(appModel: AppModel): Result<Pair<Int, Int>> {
        val diseases = appModel.sources.flatMap { source ->
            source.diseases.onEach {
                it.source = source.name
            }
        }
        return insertDiseases.insertAll(diseases)
    }

    private suspend fun importFeats(appModel: AppModel): Result<Pair<Int, Int>> {
        val feats = appModel.sources.flatMap { source ->
            source.feats.onEach {
                it.source = source.name
            }
        }
        return insertFeats.insertAll(feats)
    }

    private suspend fun importSpells(appModel: AppModel): Result<Pair<Int, Int>> {
        val spells = appModel.sources.flatMap { source ->
            source.spells.onEach {
                it.source = source.name
            }
        }
        return insertSpells.insertAll(spells)
    }

    private suspend fun importWeapons(appModel: AppModel): Result<Pair<Int, Int>> {
        val weapons = appModel.sources.flatMap { source ->
            source.weapons.onEach {
                it.source = source.name
            }
        }
        return insertWeapons.insertAll(weapons)
    }

    private suspend fun importMagicItems(appModel: AppModel): Result<Pair<Int, Int>> {
        val magicItems = appModel.sources.flatMap { source ->
            source.magicItems.onEach {
                it.source = source.name
            }
        }
        return insertMagicItems.insertAll(magicItems)
    }
}