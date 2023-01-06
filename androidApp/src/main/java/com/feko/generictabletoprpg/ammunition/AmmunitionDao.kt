package com.feko.generictabletoprpg.ammunition

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao

@Dao
abstract class AmmunitionDao
    : BaseDao<AmmunitionEntity, Ammunition>(),
    InsertAmmunitionsPort,
    GetAllAmmunitionsPort,
    GetAmmunitionByIdPort {
    override fun getEntityFromCoreModel(item: Ammunition): AmmunitionEntity =
        AmmunitionEntity.fromCoreModel(item)

    @Query("select id from ammunitions where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from ammunitions order by name")
    abstract override fun getAllSortedByNameInternal(): List<AmmunitionEntity>

    @Query("select * from ammunitions where id = :id")
    abstract override fun getByIdInternal(id: Long): AmmunitionEntity
}