package com.feko.generictabletoprpg.features.condition

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao

@Dao
abstract class ConditionDao
    : BaseDao<ConditionEntity, Condition>(),
    IInsertAllDao<Condition>,
    IGetAllDao<Condition>,
    IGetByIdDao<Condition> {
    override fun getEntityFromCoreModel(item: Condition): ConditionEntity =
        ConditionEntity.fromCoreModel(item)

    @Query("select id from conditions where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from conditions order by name")
    abstract override fun getAllSortedByNameInternal(): List<ConditionEntity>

    @Query("select * from conditions where id = :id")
    abstract override fun getByIdInternal(id: Long): ConditionEntity
}