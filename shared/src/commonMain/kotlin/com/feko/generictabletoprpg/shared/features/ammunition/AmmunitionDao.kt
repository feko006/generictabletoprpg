package com.feko.generictabletoprpg.shared.features.ammunition

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.shared.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AmmunitionDao
    : BaseDao<AmmunitionEntity, Ammunition>(),
    IInsertAllDao<Ammunition>,
    IGetAllDao<Ammunition>,
    IGetByIdDao<Ammunition> {
    override fun getEntityFromCoreModel(item: Ammunition): AmmunitionEntity =
        AmmunitionEntity.fromCoreModel(item)

    @Query("select id from ammunitions where name = :name")
    abstract override suspend fun getEntityIdByName(name: String): Long?

    @Query("select * from ammunitions order by name")
    abstract override fun getAllSortedByNameInternal(): Flow<List<AmmunitionEntity>>

    @Query("select * from ammunitions where id = :id")
    abstract override suspend fun getByIdInternal(id: Long): AmmunitionEntity
}