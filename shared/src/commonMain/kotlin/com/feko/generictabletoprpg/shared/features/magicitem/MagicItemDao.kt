package com.feko.generictabletoprpg.shared.features.magicitem

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.shared.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MagicItemDao
    : BaseDao<MagicItemEntity, MagicItem>(),
    IGetByIdDao<MagicItem>,
    IInsertAllDao<MagicItem> {
    override fun getEntityFromCoreModel(item: MagicItem): MagicItemEntity =
        MagicItemEntity.fromCoreModel(item)

    @Query("select id from magic_items where name = :name")
    abstract override suspend fun getEntityIdByName(name: String): Long?

    @Query("select * from magic_items order by name")
    abstract override fun getAllSortedByNameInternal(): Flow<List<MagicItemEntity>>

    @Query("select * from magic_items where id = :id")
    abstract override suspend fun getByIdInternal(id: Long): MagicItemEntity
}