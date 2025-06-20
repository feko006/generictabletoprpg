package com.feko.generictabletoprpg.features.tracker

import android.content.Context
import androidx.annotation.RawRes
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.feko.generictabletoprpg.common.data.MoshiJson
import com.feko.generictabletoprpg.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.features.io.domain.usecase.JsonImportAllUseCase
import com.feko.generictabletoprpg.features.tracker.domain.model.AbilityTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.HealthTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.HitDiceTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.NumberTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.PercentageTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.SpellListTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.SpellSlotTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.StatsTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.TextTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThingGroup
import com.feko.generictabletoprpg.features.tracker.ui.TrackerGroupExportSubViewModel
import com.feko.generictabletoprpg.test.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream

@RunWith(AndroidJUnit4::class)
class ImportExportTrackerDataTest {
    private lateinit var context: Context
    private lateinit var db: GenericTabletopRpgDatabase
    private lateinit var trackedThingGroupDao: TrackedThingGroupDao
    private lateinit var trackedThingDao: TrackedThingDao
    private lateinit var exportSubViewModel: TrackerGroupExportSubViewModel
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
        val json = MoshiJson()
        exportSubViewModel =
            TrackerGroupExportSubViewModel(
                trackedThingGroupDao,
                trackedThingDao,
                json
            )
        jsonImportAllUseCase =
            JsonImportAllUseCase(
                json,
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
    fun importAbilityFromResource() {
        // Given
        val data = getRawResourceData(R.raw.import_ability)
        val expected = AbilityTrackedThing(0L, "ability", 1, 1).apply { defaultValue = "2" }

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        assertThat(trackedThingGroup.name, equalTo("import_ability_group"))
        val importedTrackedThing = trackedThingDao.getById(1)
        assertAbilityEqual(importedTrackedThing, expected)
    }

    @Test
    fun importHealthFromResource() {
        // Given
        val data = getRawResourceData(R.raw.import_health)
        val expected = HealthTrackedThing(3, 0L, "health", 5, 2).apply { defaultValue = "10" }

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        assertThat(trackedThingGroup.name, equalTo("import_health_group"))
        val importedTrackedThing = trackedThingDao.getById(1) as HealthTrackedThing
        assertHealthEqual(importedTrackedThing, expected)
    }

    @Test
    fun importNumberFromResource() {
        // Given
        val data = getRawResourceData(R.raw.import_number)
        val expected = NumberTrackedThing(0L, "number", 100, 3).apply { defaultValue = "200" }

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        assertThat(trackedThingGroup.name, equalTo("import_number_group"))
        val importedTrackedThing = trackedThingDao.getById(1) as NumberTrackedThing
        assertTrackedThingEqual(importedTrackedThing, expected)
    }

    @Test
    fun importPercentageFromResource() {
        // Given
        val data = getRawResourceData(R.raw.import_percentage)

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        assertThat(trackedThingGroup.name, equalTo("import_percentage_group"))
        val importedTrackedThing = trackedThingDao.getById(1) as PercentageTrackedThing
        val expected = PercentageTrackedThing(0L, "percentage", 55.5f, 4).apply { defaultValue = "30" }
        assertPercentageEqual(importedTrackedThing, expected)
    }

    @Test
    fun importSpellSlotFromResource() {
        // Given
        val data = getRawResourceData(R.raw.import_spell_slot)
        val expected = SpellSlotTrackedThing(4, 0L, "spell_slot", 3, 5).apply { defaultValue = "5" }

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        assertThat(trackedThingGroup.name, equalTo("import_spell_slot_group"))
        val importedTrackedThing = trackedThingDao.getById(1) as SpellSlotTrackedThing
        assertSpellSlotEqual(importedTrackedThing, expected)
    }

    @Test
    fun importSpellListFromResource() {
        // Given
        val data = getRawResourceData(R.raw.import_spell_list)
        val expected = SpellListTrackedThing(0L, "spell_list", "value", 6)

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        assertThat(trackedThingGroup.name, equalTo("import_spell_list_group"))
        val importedTrackedThing = trackedThingDao.getById(1) as SpellListTrackedThing
        assertSpellListEqual(importedTrackedThing, expected)
    }

    @Test
    fun importStatsFromResource() {
        // Given
        val data = getRawResourceData(R.raw.import_stats)
        val expected = StatsTrackedThing(0L, "stats", "value", 7)

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        assertThat(trackedThingGroup.name, equalTo("import_stats_group"))
        val importedTrackedThing = trackedThingDao.getById(1) as StatsTrackedThing
        assertStatsEqual(importedTrackedThing, expected)
    }

    @Test
    fun importTextFromResource() {
        // Given
        val data = getRawResourceData(R.raw.import_text)
        val expected = TextTrackedThing(0L, "text", "Text value", 4)

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        assertThat(trackedThingGroup.name, equalTo("import_text_group"))
        val importedTrackedThing = trackedThingDao.getById(1) as TextTrackedThing
        assertTextEquals(importedTrackedThing, expected)
    }

    @Test
    fun importHitDiceFromResource() {
        // Given
        val data = getRawResourceData(R.raw.import_hit_dice)
        val expected = HitDiceTrackedThing(0L, "hit_dice", 3, 5).apply { defaultValue = "4" }

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        assertThat(trackedThingGroup.name, equalTo("import_hit_dice_group"))
        val importedTrackedThing = trackedThingDao.getById(1) as HitDiceTrackedThing
        assertHitDiceEquals(importedTrackedThing, expected)
    }

    @Test
    fun exportingThenImportingDataEnsuresSameValues() = runTest {
        // Given
        val trackedThingGroup = TrackedThingGroup(name = "ttg")
        val originalTrackedThingGroupId = trackedThingGroupDao.insertOrUpdate(trackedThingGroup)
        val ability =
            AbilityTrackedThing(0L, "ability", 1, 0, originalTrackedThingGroupId)
                .apply { defaultValue = "8" }
        val health =
            HealthTrackedThing(2, 0L, "health", 3, 1, originalTrackedThingGroupId)
                .apply { defaultValue = "9" }
        val number =
            NumberTrackedThing(0L, "number", 4, 2, originalTrackedThingGroupId)
                .apply { defaultValue = "10" }
        val percentage =
            PercentageTrackedThing(0L, "percentage", 5.5f, 3, originalTrackedThingGroupId)
                .apply { defaultValue = "11" }
        val spellSlot =
            SpellSlotTrackedThing(6, 0L, "spell_slot", 7, 4, originalTrackedThingGroupId)
                .apply { defaultValue = "12" }
        val spellList =
            SpellListTrackedThing(0L, "spell_list", "value", 5, originalTrackedThingGroupId)
        val trackedThings = listOf(ability, health, number, percentage, spellSlot, spellList)
        trackedThingDao.insertAll(trackedThings)
        val outputStream = ByteArrayOutputStream()

        // When
        exportSubViewModel.exportAllRequested()
        delay(timeMillis = 100) // The user needs to pick a file, so a delay is simulated
        exportSubViewModel.exportData(outputStream)
        jsonImportAllUseCase.import(outputStream.toString())

        // Then
        val allTrackedThingGroups = trackedThingGroupDao.getAllSortedByName()
        assertThat(allTrackedThingGroups.size, equalTo(2))
        assertThat(allTrackedThingGroups[0].name, equalTo(allTrackedThingGroups[1].name))
        val importedTrackedThingGroupId =
            allTrackedThingGroups
                .first { it.id != originalTrackedThingGroupId }
                .id
        val importedTrackedThings =
            trackedThingDao.getAllSortedByIndex(importedTrackedThingGroupId)
        assertThat(trackedThings.size, equalTo(importedTrackedThings.size))
        assertAbilityEqual(importedTrackedThings[0], ability)
        assertHealthEqual(importedTrackedThings[1], health)
        assertNumberEqual(importedTrackedThings[2], number)
        assertPercentageEqual(importedTrackedThings[3], percentage)
        assertSpellSlotEqual(importedTrackedThings[4], spellSlot)
        assertSpellListEqual(importedTrackedThings[5], spellList)
    }

    private fun assertAbilityEqual(actual: TrackedThing, expected: AbilityTrackedThing) {
        assertThat(actual, instanceOf(expected::class.java))
        assertTrackedThingEqual(actual, expected)
    }

    private fun assertHealthEqual(actual: TrackedThing, expected: HealthTrackedThing) {
        assertThat(actual, instanceOf(expected::class.java))
        assertTrackedThingEqual(actual, expected)
        val health = actual as HealthTrackedThing
        assertThat(health.temporaryHp, equalTo(expected.temporaryHp))
    }

    private fun assertPercentageEqual(actual: TrackedThing, expected: PercentageTrackedThing) {
        assertThat(actual, instanceOf(expected::class.java))
        assertTrackedThingEqual(actual, expected)
    }

    private fun assertNumberEqual(actual: TrackedThing, expected: NumberTrackedThing) {
        assertThat(actual, instanceOf(expected::class.java))
        assertTrackedThingEqual(actual, expected)
    }

    private fun assertSpellSlotEqual(actual: TrackedThing, expected: SpellSlotTrackedThing) {
        assertThat(actual, instanceOf(expected::class.java))
        assertTrackedThingEqual(actual, expected)
        val spellSlot = actual as SpellSlotTrackedThing
        assertThat(spellSlot.level, equalTo(expected.level))
    }

    private fun assertSpellListEqual(actual: TrackedThing, expected: SpellListTrackedThing) {
        assertThat(actual, instanceOf(expected::class.java))
        assertTrackedThingEqual(actual, expected)
    }

    private fun assertStatsEqual(actual: TrackedThing, expected: StatsTrackedThing) {
        assertThat(actual, instanceOf(expected::class.java))
        assertTrackedThingEqual(actual, expected)
    }

    private fun assertTextEquals(actual: TrackedThing, expected: TextTrackedThing) {
        assertThat(actual, instanceOf(expected::class.java))
        assertTrackedThingEqual(actual, expected)
    }

    private fun assertHitDiceEquals(actual: TrackedThing, expected: HitDiceTrackedThing) {
        assertThat(actual, instanceOf(expected::class.java))
        assertTrackedThingEqual(actual, expected)
    }

    private fun assertTrackedThingEqual(actual: TrackedThing, expected: TrackedThing) {
        actual.run {
            assertThat(name, equalTo(expected.name))
            assertThat(value, equalTo(expected.value))
            assertThat(type, equalTo(expected.type))
            assertThat(index, equalTo(expected.index))
            assertThat(defaultValue, equalTo(expected.defaultValue))
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
