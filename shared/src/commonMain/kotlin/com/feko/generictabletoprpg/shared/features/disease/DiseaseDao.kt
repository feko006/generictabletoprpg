package com.feko.generictabletoprpg.shared.features.disease

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.shared.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao

@Dao
abstract class DiseaseDao
    : BaseDao<DiseaseEntity, Disease>(),
    IInsertAllDao<Disease>,
    IGetAllDao<Disease>,
    IGetByIdDao<Disease> {
    override fun getEntityFromCoreModel(item: Disease): DiseaseEntity =
        DiseaseEntity.fromCoreModel(item)

    @Query("select id from diseases where name = :name")
    abstract override suspend fun getEntityIdByName(name: String): Long?

    @Query("select * from diseases order by name")
    abstract override suspend fun getAllSortedByNameInternal(): List<DiseaseEntity>

    @Query("select * from diseases where id = :id")
    abstract override suspend fun getByIdInternal(id: Long): DiseaseEntity
}