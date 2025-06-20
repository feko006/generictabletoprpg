package com.feko.generictabletoprpg.features.disease

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.data.local.BaseDao
import com.feko.generictabletoprpg.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.common.data.local.IInsertAllDao

@Dao
abstract class DiseaseDao
    : BaseDao<DiseaseEntity, Disease>(),
    IInsertAllDao<Disease>,
    IGetAllDao<Disease>,
    IGetByIdDao<Disease> {
    override fun getEntityFromCoreModel(item: Disease): DiseaseEntity =
        DiseaseEntity.fromCoreModel(item)

    @Query("select id from diseases where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from diseases order by name")
    abstract override fun getAllSortedByNameInternal(): List<DiseaseEntity>

    @Query("select * from diseases where id = :id")
    abstract override fun getByIdInternal(id: Long): DiseaseEntity
}