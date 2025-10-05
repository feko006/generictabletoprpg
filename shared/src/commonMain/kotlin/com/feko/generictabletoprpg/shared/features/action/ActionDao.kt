package com.feko.generictabletoprpg.shared.features.action

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.shared.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao

@Dao
abstract class ActionDao
    : BaseDao<ActionEntity, Action>(),
    IInsertAllDao<Action>,
    IGetAllDao<Action>,
    IGetByIdDao<Action> {
    override fun getEntityFromCoreModel(item: Action): ActionEntity =
        ActionEntity.fromCoreModel(item)

    @Query("select id from actions where name = :name")
    abstract override suspend fun getEntityIdByName(name: String): Long?

    @Query("select * from actions order by name")
    abstract override suspend fun getAllSortedByNameInternal(): List<ActionEntity>

    @Query("select * from actions where id = :id")
    abstract override suspend fun getByIdInternal(id: Long): ActionEntity
}