package com.feko.generictabletoprpg.tracker

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.IDelete
import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.IInsertOrUpdate

@Dao
abstract class TrackedThingGroupDao :
    BaseDao<TrackedThingGroupEntity, TrackedThingGroup>(),
    IGetAll<TrackedThingGroup>,
    IInsertOrUpdate<TrackedThingGroup>,
    IDelete {

    override fun getEntityFromCoreModel(item: TrackedThingGroup): TrackedThingGroupEntity =
        TrackedThingGroupEntity(item.id, item.name)

    @Query("select * from tracked_thing_groups order by name")
    abstract override fun getAllSortedByNameInternal(): List<TrackedThingGroupEntity>

    @Query("select * from tracked_thing_groups where id = :id")
    abstract override fun getByIdInternal(id: Long): TrackedThingGroupEntity

    @Query("delete from tracked_thing_groups where id = :id")
    abstract override fun delete(id: Long)

    override fun getEntityId(entity: TrackedThingGroupEntity): Long? =
        if (entity.id > 0) entity.id else null
}

