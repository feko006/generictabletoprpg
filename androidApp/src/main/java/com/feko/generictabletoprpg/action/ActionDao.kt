package com.feko.generictabletoprpg.action

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao

@Dao
abstract class ActionDao
    : BaseDao<ActionEntity, Action>(),
    InsertActionsPort {
    override fun getEntityFromCoreModel(item: Action): ActionEntity =
        ActionEntity.fromCoreModel(item)

    @Query("select id from actions where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?
}