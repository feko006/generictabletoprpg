package com.feko.generictabletoprpg.tracker

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.feko.generictabletoprpg.import.JsonImportAllUseCase
import com.feko.generictabletoprpg.import.MoshiJson
import com.feko.generictabletoprpg.room.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.test.R
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImportExportTrackerDataTest {
    private lateinit var context: Context
    private lateinit var db: GenericTabletopRpgDatabase
    private lateinit var trackedThingGroupDao: TrackedThingGroupDao
    private lateinit var trackedThingDao: TrackedThingDao
    private lateinit var trackerGroupExportViewModelExtension: TrackerGroupExportViewModelExtension
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
        trackerGroupExportViewModelExtension =
            TrackerGroupExportViewModelExtension(
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
        val inputStream = context.resources.openRawResource(R.raw.import_ability)
        val data = inputStream
            .bufferedReader()
            .use { br ->
                br.readText()
            }

        // When
        jsonImportAllUseCase.import(data)

        // Then
        val trackedThingGroup = trackedThingGroupDao.getById(1)
        assertEquals("import_ability_group", trackedThingGroup.name)
        val importedTrackedThing = trackedThingDao.getById(1)
        assertEquals("ability", importedTrackedThing.name)
        assertEquals("1", importedTrackedThing.value)
        assertEquals(TrackedThing.Type.Ability, importedTrackedThing.type)
        assertEquals(1, importedTrackedThing.index)
        assertEquals("2", importedTrackedThing.defaultValue)
    }
}