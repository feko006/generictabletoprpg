package com.feko.generictabletoprpg.action

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.IGetById
import com.feko.generictabletoprpg.common.IInsertAll

@Dao
abstract class ActionDao
    : BaseDao<ActionEntity, Action>(),
    IInsertAll<Action>,
    IGetAll<Action>,
    IGetById<Action> {
    override fun getEntityFromCoreModel(item: Action): ActionEntity =
        ActionEntity.fromCoreModel(item)

    @Query("select id from actions where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from actions order by name")
    abstract override fun getAllSortedByNameInternal(): List<ActionEntity>

    @Query("select * from actions where id = :id")
    abstract override fun getByIdInternal(id: Long): ActionEntity
}