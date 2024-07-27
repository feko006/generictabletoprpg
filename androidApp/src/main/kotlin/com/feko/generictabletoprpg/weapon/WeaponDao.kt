package com.feko.generictabletoprpg.weapon

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.IGetById
import com.feko.generictabletoprpg.common.IInsertAll
import com.feko.generictabletoprpg.weapon.*

@Dao
abstract class WeaponDao
    : BaseDao<WeaponEntity, Weapon>(),
    IInsertAll<Weapon>,
    IGetAll<Weapon>,
    IGetById<Weapon> {
    override fun getEntityFromCoreModel(item: Weapon): WeaponEntity =
        WeaponEntity.fromCoreModel(item)

    @Query("select id from weapons where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from weapons order by name")
    abstract override fun getAllSortedByNameInternal(): List<WeaponEntity>

    @Query("select * from weapons where id = :id")
    abstract override fun getByIdInternal(id: Long): WeaponEntity
}