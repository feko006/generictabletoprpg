package com.feko.generictabletoprpg.armor

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.armor.*
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.IGetById
import com.feko.generictabletoprpg.common.IInsertAll

@Dao
abstract class ArmorDao
    : BaseDao<ArmorEntity, Armor>(),
    IInsertAll<Armor>,
    IGetAll<Armor>,
    IGetById<Armor> {
    override fun getEntityFromCoreModel(item: Armor): ArmorEntity =
        ArmorEntity.fromCoreModel(item)

    @Query("select id from armors where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from armors order by name")
    abstract override fun getAllSortedByNameInternal(): List<ArmorEntity>

    @Query("select * from armors where id = :id")
    abstract override fun getByIdInternal(id: Long): ArmorEntity
}