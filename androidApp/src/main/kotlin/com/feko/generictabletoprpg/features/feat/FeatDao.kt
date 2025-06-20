package com.feko.generictabletoprpg.features.feat

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.data.local.BaseDao
import com.feko.generictabletoprpg.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.common.data.local.IInsertAllDao

@Dao
abstract class FeatDao
    : BaseDao<FeatEntity, Feat>(),
    IInsertAllDao<Feat>,
    IGetAllDao<Feat>,
    IGetByIdDao<Feat> {
    override fun getEntityFromCoreModel(item: Feat): FeatEntity =
        FeatEntity.fromCoreModel(item)

    @Query("select id from feats where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from feats order by name")
    abstract override fun getAllSortedByNameInternal(): List<FeatEntity>

    @Query("select * from feats where id = :id")
    abstract override fun getByIdInternal(id: Long): FeatEntity
}