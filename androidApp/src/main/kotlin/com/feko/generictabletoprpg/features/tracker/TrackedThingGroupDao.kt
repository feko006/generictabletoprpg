package com.feko.generictabletoprpg.features.tracker

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.data.local.BaseDao
import com.feko.generictabletoprpg.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.common.data.local.IInsertOrUpdateDao
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThingGroup

@Dao
abstract class TrackedThingGroupDao :
    BaseDao<TrackedThingGroupEntity, TrackedThingGroup>(),
    IGetAllDao<TrackedThingGroup>,
    IInsertOrUpdateDao<TrackedThingGroup> {

    override fun getEntityFromCoreModel(item: TrackedThingGroup): TrackedThingGroupEntity =
        item.toEntity()

    @Query("select * from tracked_thing_groups order by name")
    abstract override fun getAllSortedByNameInternal(): List<TrackedThingGroupEntity>

    @Query("select * from tracked_thing_groups where id = :id")
    abstract override fun getByIdInternal(id: Long): TrackedThingGroupEntity

    @Query("delete from tracked_thing_groups where id = :id")
    abstract fun delete(id: Long)

    override fun getEntityId(entity: TrackedThingGroupEntity): Long? =
        if (entity.id > 0) entity.id else null
}

