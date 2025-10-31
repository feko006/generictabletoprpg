package com.feko.generictabletoprpg.shared.features.condition

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.shared.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ConditionDao
    : BaseDao<ConditionEntity, Condition>(),
    IInsertAllDao<Condition>,
    IGetAllDao<Condition>,
    IGetByIdDao<Condition> {
    override fun getEntityFromCoreModel(item: Condition): ConditionEntity =
        ConditionEntity.fromCoreModel(item)

    @Query("select id from conditions where name = :name")
    abstract override suspend fun getEntityIdByName(name: String): Long?

    @Query("select * from conditions order by name")
    abstract override fun getAllSortedByNameInternal(): Flow<List<ConditionEntity>>

    @Query("select * from conditions where id = :id")
    abstract override suspend fun getByIdInternal(id: Long): ConditionEntity
}