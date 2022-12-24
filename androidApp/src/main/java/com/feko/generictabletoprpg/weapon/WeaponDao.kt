package com.feko.generictabletoprpg.weapon

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.weapon.*

@Dao
abstract class WeaponDao
    : BaseDao<WeaponEntity, Weapon>(),
    InsertWeaponsPort,
    GetAllWeaponsPort,
    GetWeaponByIdPort {
    override fun getEntityFromCoreModel(item: Weapon): WeaponEntity =
        WeaponEntity.fromCoreModel(item)

    @Query("select id from weapons where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from weapons order by name")
    abstract override fun getAllSortedByNameInternal(): List<WeaponEntity>

    @Query("select * from weapons where id = :id")
    abstract override fun getByIdInternal(id: Long): WeaponEntity
}