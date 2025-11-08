package com.feko.generictabletoprpg.shared.features.feat

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.shared.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FeatDao
    : BaseDao<FeatEntity, Feat>(),
    IInsertAllDao<Feat>,
    IGetAllDao<Feat>,
    IGetByIdDao<Feat> {
    override fun getEntityFromCoreModel(item: Feat): FeatEntity =
        FeatEntity.fromCoreModel(item)

    @Query("select id from feats where name = :name")
    abstract override suspend fun getEntityIdByName(name: String): Long?

    @Query("select * from feats order by name")
    abstract override fun getAllSortedByNameInternal(): Flow<List<FeatEntity>>

    @Query("select * from feats where id = :id")
    abstract override suspend fun getByIdInternal(id: Long): FeatEntity
}