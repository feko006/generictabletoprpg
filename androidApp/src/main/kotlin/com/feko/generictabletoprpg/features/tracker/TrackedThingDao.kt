package com.feko.generictabletoprpg.features.tracker

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllByParentSortedByIndexDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertOrUpdateDao
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThing

@Dao
abstract class TrackedThingDao :
    BaseDao<TrackedThingEntity, TrackedThing>(),
    IGetAllByParentSortedByIndexDao<TrackedThing>,
    IInsertOrUpdateDao<TrackedThing> {
    override fun getEntityFromCoreModel(item: TrackedThing): TrackedThingEntity {
        var level = 0
        if (item.type == TrackedThing.Type.SpellSlot) {
            level = item.level
        }
        var temporaryHp = 0
        if (item.type == TrackedThing.Type.Health) {
            temporaryHp = item.temporaryHp
        }
        val type = item.type.ordinal
        return TrackedThingEntity(
            item.id,
            item.name,
            level,
            temporaryHp,
            item.value,
            item.managedDefaultValue,
            type,
            item.index,
            item.groupId
        )
    }

    @Query("select * from tracked_things where id = :id")
    abstract override fun getByIdInternal(id: Long): TrackedThingEntity

    @Query("delete from tracked_things where id = :id")
    abstract fun delete(id: Long)

    override fun getEntityId(entity: TrackedThingEntity): Long? =
        if (entity.id > 0) entity.id else null

    @Query("select * from tracked_things where groupId = :parentId order by idx")
    abstract fun getAllSortedByIndexInternal(parentId: Long): List<TrackedThingEntity>

    override fun getAllSortedByIndex(parentId: Long): List<TrackedThing> =
        getAllSortedByIndexInternal(parentId)
            .map { it.toCoreModel() }
}