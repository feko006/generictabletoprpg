package com.feko.generictabletoprpg.condition

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.IGetById
import com.feko.generictabletoprpg.common.IInsertAll

@Dao
abstract class ConditionDao
    : BaseDao<ConditionEntity, Condition>(),
    IInsertAll<Condition>,
    IGetAll<Condition>,
    IGetById<Condition> {
    override fun getEntityFromCoreModel(item: Condition): ConditionEntity =
        ConditionEntity.fromCoreModel(item)

    @Query("select id from conditions where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from conditions order by name")
    abstract override fun getAllSortedByNameInternal(): List<ConditionEntity>

    @Query("select * from conditions where id = :id")
    abstract override fun getByIdInternal(id: Long): ConditionEntity
}