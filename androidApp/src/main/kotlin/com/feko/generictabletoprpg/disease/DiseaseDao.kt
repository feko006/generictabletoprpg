package com.feko.generictabletoprpg.disease

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.IGetById
import com.feko.generictabletoprpg.common.IInsertAll

@Dao
abstract class DiseaseDao
    : BaseDao<DiseaseEntity, Disease>(),
    IInsertAll<Disease>,
    IGetAll<Disease>,
    IGetById<Disease> {
    override fun getEntityFromCoreModel(item: Disease): DiseaseEntity =
        DiseaseEntity.fromCoreModel(item)

    @Query("select id from diseases where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from diseases order by name")
    abstract override fun getAllSortedByNameInternal(): List<DiseaseEntity>

    @Query("select * from diseases where id = :id")
    abstract override fun getByIdInternal(id: Long): DiseaseEntity
}