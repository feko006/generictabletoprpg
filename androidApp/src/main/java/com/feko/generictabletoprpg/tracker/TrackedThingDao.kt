package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.tracker.DeleteTrackedThingPort
import com.feko.generictabletoprpg.tracker.GetAllTrackedThingsByGroupPort
import com.feko.generictabletoprpg.tracker.Health
import com.feko.generictabletoprpg.tracker.InsertOrUpdateTrackedThingPort
import com.feko.generictabletoprpg.tracker.SpellSlot
import com.feko.generictabletoprpg.tracker.TrackedThing

@Dao
abstract class TrackedThingDao :
    BaseDao<TrackedThingEntity, TrackedThing>(),
    GetAllTrackedThingsByGroupPort,
    InsertOrUpdateTrackedThingPort,
    DeleteTrackedThingPort {
    override fun getEntityFromCoreModel(item: TrackedThing): TrackedThingEntity {
        var level = 0
        if (item is SpellSlot) {
            level = item.level
        }
        var temporaryHp = 0
        if (item is Health) {
            temporaryHp = item.temporaryHp
        }
        val type = item.type.ordinal
        return TrackedThingEntity(
            item.id,
            item.name,
            level,
            temporaryHp,
            item.value,
            item.defaultValue,
            type,
            item.groupId
        )
    }

    @Query("select * from tracked_things where groupId = :parentId order by name")
    abstract override fun getAllSortedByNameInternal(parentId: Long): List<TrackedThingEntity>

    @Query("select * from tracked_things where id = :id")
    abstract override fun getByIdInternal(id: Long): TrackedThingEntity

    @Query("delete from tracked_things where id = :id")
    abstract override fun delete(id: Long)

    override fun getEntityId(entity: TrackedThingEntity): Long? =
        if (entity.id > 0) entity.id else null
}