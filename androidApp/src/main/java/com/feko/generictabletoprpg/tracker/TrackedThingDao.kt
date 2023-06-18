package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingsPort
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingPort
import com.feko.generictabletoprpg.tracker.TrackedThing

@Dao
abstract class TrackedThingDao :
    BaseDao<TrackedThingEntity, TrackedThing>(),
    GetAllTrackedThingsPort,
    InsertOrUpdateTrackedThingPort {
    override fun getEntityFromCoreModel(item: TrackedThing): TrackedThingEntity {
        var level = 0
        if (item is TrackedThing.SpellSlot) {
            level = item.level
        }
        val type = item.type.ordinal
        return TrackedThingEntity(item.id, item.name, level, item.value, item.defaultValue, type)
    }

    @Query("select * from tracked_things order by name")
    abstract override fun getAllSortedByNameInternal(): List<TrackedThingEntity>

    @Query("select * from tracked_things where id = :id")
    abstract override fun getByIdInternal(id: Long): TrackedThingEntity

    override fun getEntityId(entity: TrackedThingEntity): Long? =
        if (entity.id > 0) entity.id else null
}