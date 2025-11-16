package com.feko.generictabletoprpg.shared.features.tracker

import android.content.Context
import androidx.annotation.RawRes
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.JsonImportAllUseCase
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup
import com.feko.generictabletoprpg.shared.features.tracker.ui.TrackerGroupViewModel
import com.feko.generictabletoprpg.shared.test.R
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ImportExportTrackerDataTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var context: Context
    private lateinit var db: GenericTabletopRpgDatabase
    private lateinit var trackedThingGroupDao: TrackedThingGroupDao
    private lateinit var trackedThingDao: TrackedThingDao
    private lateinit var viewModel: TrackerGroupViewModel
    private lateinit var jsonImportAllUseCase: JsonImportAllUseCase

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        db = Room
            .inMemoryDatabaseBuilder(
                context,
                GenericTabletopRpgDatabase::class.java
            )
            .build()
        trackedThingGroupDao = db.trackedThingGroupDao()
        trackedThingDao = db.trackedThingDao()
        viewModel = TrackerGroupViewModel(trackedThingGroupDao, trackedThingDao)
        jsonImportAllUseCase =
            JsonImportAllUseCase(
                db.actionDao(),
                db.conditionDao(),
                db.diseaseDao(),
                db.trackedThingGroupDao(),
                db.trackedThingDao()
            )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun importAbilityFromResource() = runTest {
        // Given
        val data = getRawResourceData(R.raw.import_ability)
        val expected =
            TrackedThing(0L, "ability", "1", TrackedThing.Type.Ability, 1, defaultValue = "2")

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        MatcherAssert.assertThat(
            trackedThingGroup.name,
            CoreMatchers.equalTo("import_ability_group")
        )
        val importedTrackedThing = trackedThingDao.getById(1)
        assertTrackedThingEqual(importedTrackedThing, expected)
    }

    @Test
    fun importHealthFromResource() = runTest {
        // Given
        val data = getRawResourceData(R.raw.import_health)
        val expected = TrackedThing(
            0L,
            "health",
            "5",
            TrackedThing.Type.Health,
            2,
            temporaryHp = 3,
            defaultValue = "10"
        )

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        MatcherAssert.assertThat(
            trackedThingGroup.name,
            CoreMatchers.equalTo("import_health_group")
        )
        val importedTrackedThing = trackedThingDao.getById(1)
        assertHealthEqual(importedTrackedThing, expected)
    }

    @Test
    fun importNumberFromResource() = runTest {
        // Given
        val data = getRawResourceData(R.raw.import_number)
        val expected =
            TrackedThing(0L, "number", "100", TrackedThing.Type.Number, 3, defaultValue = "200")

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        MatcherAssert.assertThat(
            trackedThingGroup.name,
            CoreMatchers.equalTo("import_number_group")
        )
        val importedTrackedThing = trackedThingDao.getById(1)
        assertTrackedThingEqual(importedTrackedThing, expected)
    }

    @Test
    fun importPercentageFromResource() = runTest {
        // Given
        val data = getRawResourceData(R.raw.import_percentage)

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        MatcherAssert.assertThat(
            trackedThingGroup.name,
            CoreMatchers.equalTo("import_percentage_group")
        )
        val importedTrackedThing = trackedThingDao.getById(1)
        val expected =
            TrackedThing(
                0L,
                "percentage",
                "55.5",
                TrackedThing.Type.Percentage,
                4,
                defaultValue = "30"
            )
        assertTrackedThingEqual(importedTrackedThing, expected)
    }

    @Test
    fun importSpellSlotFromResource() = runTest {
        // Given
        val data = getRawResourceData(R.raw.import_spell_slot)
        val expected = TrackedThing(
            0L,
            "spell_slot",
            "3",
            TrackedThing.Type.SpellSlot,
            5,
            level = 4,
            defaultValue = "5"
        )

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        MatcherAssert.assertThat(
            trackedThingGroup.name,
            CoreMatchers.equalTo("import_spell_slot_group")
        )
        val importedTrackedThing = trackedThingDao.getById(1)
        assertSpellSlotEqual(importedTrackedThing, expected)
    }

    @Test
    fun importSpellListFromResource() = runTest {
        // Given
        val data = getRawResourceData(R.raw.import_spell_list)
        val expected = TrackedThing(0L, "spell_list", "value", TrackedThing.Type.SpellList, 6)

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        MatcherAssert.assertThat(
            trackedThingGroup.name,
            CoreMatchers.equalTo("import_spell_list_group")
        )
        val importedTrackedThing = trackedThingDao.getById(1)
        assertTrackedThingEqual(importedTrackedThing, expected)
    }

    @Test
    fun importStatsFromResource() = runTest {
        // Given
        val data = getRawResourceData(R.raw.import_stats)
        val expected = TrackedThing(0L, "stats", "value", TrackedThing.Type.FiveEStats, 7)

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        MatcherAssert.assertThat(trackedThingGroup.name, CoreMatchers.equalTo("import_stats_group"))
        val importedTrackedThing = trackedThingDao.getById(1)
        assertTrackedThingEqual(importedTrackedThing, expected)
    }

    @Test
    fun importTextFromResource() = runTest {
        // Given
        val data = getRawResourceData(R.raw.import_text)
        val expected = TrackedThing(0L, "text", "Text value", TrackedThing.Type.Text, 4)

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        MatcherAssert.assertThat(trackedThingGroup.name, CoreMatchers.equalTo("import_text_group"))
        val importedTrackedThing = trackedThingDao.getById(1)
        assertTrackedThingEqual(importedTrackedThing, expected)
    }

    @Test
    fun importHitDiceFromResource() = runTest {
        // Given
        val data = getRawResourceData(R.raw.import_hit_dice)
        val expected =
            TrackedThing(0L, "hit_dice", "3", TrackedThing.Type.HitDice, 5, defaultValue = "4")

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        MatcherAssert.assertThat(
            trackedThingGroup.name,
            CoreMatchers.equalTo("import_hit_dice_group")
        )
        val importedTrackedThing = trackedThingDao.getById(1)
        assertTrackedThingEqual(importedTrackedThing, expected)
    }

    @Test
    fun exportingThenImportingDataEnsuresSameValues() = runTest {
        // Given
        val trackedThingGroup = TrackedThingGroup(name = "ttg")
        val originalTrackedThingGroupId = trackedThingGroupDao.insertOrUpdate(trackedThingGroup)
        val ability =
            TrackedThing(
                0L,
                "ability",
                "1",
                TrackedThing.Type.Ability,
                0,
                groupId = originalTrackedThingGroupId,
                defaultValue = "8"
            )
        val health =
            TrackedThing(
                0L,
                "health",
                "3",
                TrackedThing.Type.Health,
                1,
                groupId = originalTrackedThingGroupId,
                temporaryHp = 2,
                defaultValue = "9"
            )
        val number =
            TrackedThing(
                0L,
                "number",
                "4",
                TrackedThing.Type.Number,
                2,
                groupId = originalTrackedThingGroupId,
                defaultValue = "10"
            )
        val percentage =
            TrackedThing(
                0L,
                "percentage",
                "5.5",
                TrackedThing.Type.Percentage,
                3,
                groupId = originalTrackedThingGroupId,
                defaultValue = "11"
            )
        val spellSlot =
            TrackedThing(
                0L,
                "spell_slot",
                "7",
                TrackedThing.Type.SpellSlot,
                4,
                groupId = originalTrackedThingGroupId,
                level = 6,
                defaultValue = "12"
            )
        val spellList =
            TrackedThing(
                0L,
                "spell_list",
                "value",
                TrackedThing.Type.SpellList,
                5,
                groupId = originalTrackedThingGroupId
            )
        val trackedThings = listOf(ability, health, number, percentage, spellSlot, spellList)
        trackedThingDao.insertAll(trackedThings)
        val file = PlatformFile(FileKit.filesDir, "export.json")

        // When
        viewModel.exportAll(fileSaverLauncher = null)
        viewModel.onFileSaveLocationSelected(file)
        jsonImportAllUseCase.import(file.readString())

        // Then
        val allTrackedThingGroups = trackedThingGroupDao.getAllSortedByName().first()
        MatcherAssert.assertThat(allTrackedThingGroups.size, CoreMatchers.equalTo(2))
        MatcherAssert.assertThat(
            allTrackedThingGroups[0].name,
            CoreMatchers.equalTo(allTrackedThingGroups[1].name)
        )
        val importedTrackedThingGroupId =
            allTrackedThingGroups
                .first { it.id != originalTrackedThingGroupId }
                .id
        val importedTrackedThings =
            trackedThingDao.getAllSortedByIndex(importedTrackedThingGroupId).first()
        MatcherAssert.assertThat(
            trackedThings.size,
            CoreMatchers.equalTo(importedTrackedThings.size)
        )
        assertTrackedThingEqual(importedTrackedThings[0], ability)
        assertHealthEqual(importedTrackedThings[1], health)
        assertTrackedThingEqual(importedTrackedThings[2], number)
        assertTrackedThingEqual(importedTrackedThings[3], percentage)
        assertSpellSlotEqual(importedTrackedThings[4], spellSlot)
        assertTrackedThingEqual(importedTrackedThings[5], spellList)
    }

    private fun assertHealthEqual(actual: TrackedThing, expected: TrackedThing) {
        assertTrackedThingEqual(actual, expected)
        MatcherAssert.assertThat(actual.temporaryHp, CoreMatchers.equalTo(expected.temporaryHp))
    }

    private fun assertSpellSlotEqual(actual: TrackedThing, expected: TrackedThing) {
        assertTrackedThingEqual(actual, expected)
        MatcherAssert.assertThat(actual.level, CoreMatchers.equalTo(expected.level))
    }

    private fun assertTrackedThingEqual(actual: TrackedThing, expected: TrackedThing) {
        actual.run {
            MatcherAssert.assertThat(name, CoreMatchers.equalTo(expected.name))
            MatcherAssert.assertThat(value, CoreMatchers.equalTo(expected.value))
            MatcherAssert.assertThat(type, CoreMatchers.equalTo(expected.type))
            MatcherAssert.assertThat(index, CoreMatchers.equalTo(expected.index))
            MatcherAssert.assertThat(
                managedDefaultValue,
                CoreMatchers.equalTo(expected.managedDefaultValue)
            )
        }
    }

    private fun getRawResourceData(@RawRes resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        val data = inputStream
            .bufferedReader()
            .use { br ->
                br.readText()
            }
        return data
    }
}