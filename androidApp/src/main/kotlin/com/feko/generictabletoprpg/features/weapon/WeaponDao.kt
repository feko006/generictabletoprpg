package com.feko.generictabletoprpg.features.weapon

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.data.local.BaseDao
import com.feko.generictabletoprpg.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.common.data.local.IInsertAllDao

@Dao
abstract class WeaponDao
    : BaseDao<WeaponEntity, Weapon>(),
    IInsertAllDao<Weapon>,
    IGetAllDao<Weapon>,
    IGetByIdDao<Weapon> {
    override fun getEntityFromCoreModel(item: Weapon): WeaponEntity =
        WeaponEntity.fromCoreModel(item)

    @Query("select id from weapons where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from weapons order by name")
    abstract override fun getAllSortedByNameInternal(): List<WeaponEntity>

    @Query("select * from weapons where id = :id")
    abstract override fun getByIdInternal(id: Long): WeaponEntity
}