package com.feko.generictabletoprpg.feat

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao

@Dao
abstract class FeatDao
    : BaseDao<FeatEntity, Feat>(),
    InsertFeatsPort,
    GetAllFeatsPort,
    GetFeatByIdPort {
    override fun getEntityFromCoreModel(item: Feat): FeatEntity =
        FeatEntity.fromCoreModel(item)

    @Query("select id from feats where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from feats order by name")
    abstract override fun getAllSortedByNameInternal(): List<FeatEntity>

    @Query("select * from feats where id = :id")
    abstract override fun getByIdInternal(id: Long): FeatEntity
}