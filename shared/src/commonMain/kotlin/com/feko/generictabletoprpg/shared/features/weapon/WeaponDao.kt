package com.feko.generictabletoprpg.shared.features.weapon

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.shared.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao

@Dao
abstract class WeaponDao
    : BaseDao<WeaponEntity, Weapon>(),
    IInsertAllDao<Weapon>,
    IGetAllDao<Weapon>,
    IGetByIdDao<Weapon> {
    override fun getEntityFromCoreModel(item: Weapon): WeaponEntity =
        WeaponEntity.fromCoreModel(item)

    @Query("select id from weapons where name = :name")
    abstract override suspend fun getEntityIdByName(name: String): Long?

    @Query("select * from weapons order by name")
    abstract override suspend fun getAllSortedByNameInternal(): List<WeaponEntity>

    @Query("select * from weapons where id = :id")
    abstract override suspend fun getByIdInternal(id: Long): WeaponEntity
}