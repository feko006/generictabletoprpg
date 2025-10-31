package com.feko.generictabletoprpg.shared.features.tracker

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.shared.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertOrUpdateDao
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TrackedThingGroupDao :
    BaseDao<TrackedThingGroupEntity, TrackedThingGroup>(),
    IGetAllDao<TrackedThingGroup>,
    IInsertOrUpdateDao<TrackedThingGroup> {

    override fun getEntityFromCoreModel(item: TrackedThingGroup): TrackedThingGroupEntity =
        item.toEntity()

    @Query("select * from tracked_thing_groups order by name")
    abstract override fun getAllSortedByNameInternal(): Flow<List<TrackedThingGroupEntity>>

    @Query("select * from tracked_thing_groups where id = :id")
    abstract override suspend fun getByIdInternal(id: Long): TrackedThingGroupEntity

    @Query("delete from tracked_thing_groups where id = :id")
    abstract suspend fun delete(id: Long)

    override suspend fun getEntityId(entity: TrackedThingGroupEntity): Long? =
        if (entity.id > 0) entity.id else null
}

