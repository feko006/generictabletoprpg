package com.feko.generictabletoprpg.tracker

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.IDelete
import com.feko.generictabletoprpg.common.IGetAllByParent
import com.feko.generictabletoprpg.common.IInsertOrUpdate

@Dao
abstract class TrackedThingDao :
    BaseDao<TrackedThingEntity, TrackedThing>(),
    IGetAllByParent<TrackedThing>,
    IInsertOrUpdate<TrackedThing>,
    IDelete {
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
            item.index,
            item.groupId
        )
    }

    @Query("select * from tracked_things where id = :id")
    abstract override fun getByIdInternal(id: Long): TrackedThingEntity

    @Query("delete from tracked_things where id = :id")
    abstract override fun delete(id: Long)

    override fun getEntityId(entity: TrackedThingEntity): Long? =
        if (entity.id > 0) entity.id else null

    @Query("select * from tracked_things where groupId = :parentId order by idx")
    abstract fun getAllSortedByIndexInternal(parentId: Long): List<TrackedThingEntity>

    fun getAllSortedByIndex(groupId: Long): List<TrackedThing> =
        getAllSortedByIndexInternal(groupId)
            .map { it.toCoreModel() }
}