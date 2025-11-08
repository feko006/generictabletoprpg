package com.feko.generictabletoprpg.shared.features.armor

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.shared.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ArmorDao
    : BaseDao<ArmorEntity, Armor>(),
    IInsertAllDao<Armor>,
    IGetAllDao<Armor>,
    IGetByIdDao<Armor> {
    override fun getEntityFromCoreModel(item: Armor): ArmorEntity =
        ArmorEntity.fromCoreModel(item)

    @Query("select id from armors where name = :name")
    abstract override suspend fun getEntityIdByName(name: String): Long?

    @Query("select * from armors order by name")
    abstract override fun getAllSortedByNameInternal(): Flow<List<ArmorEntity>>

    @Query("select * from armors where id = :id")
    abstract override suspend fun getByIdInternal(id: Long): ArmorEntity
}