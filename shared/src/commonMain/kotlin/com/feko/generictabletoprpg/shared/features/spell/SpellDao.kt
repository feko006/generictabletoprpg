package com.feko.generictabletoprpg.shared.features.spell

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.shared.common.data.local.BaseDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SpellDao
    : BaseDao<SpellEntity, Spell>(),
    IInsertAllDao<Spell>,
    IGetAllDao<Spell>,
    IGetByIdDao<Spell>,
    ISpellFilterDao {
    override fun getEntityFromCoreModel(item: Spell): SpellEntity =
        SpellEntity.fromCoreModel(item)

    @Query("select id from spells where name = :name")
    abstract override suspend fun getEntityIdByName(name: String): Long?

    @Query("select * from spells order by name")
    abstract override suspend fun getAllSortedByNameInternal(): List<SpellEntity>

    @Query("select * from spells where id = :id")
    abstract override suspend fun getByIdInternal(id: Long): SpellEntity

    @Query("select distinct school from spells order by school asc")
    abstract override fun getAllSchools(): Flow<List<String>>

    @Query("select distinct level from spells order by level asc")
    abstract override fun getAllLevels(): Flow<List<Int>>

    @Query("select distinct classesThatCanCast from spells")
    abstract override fun getAllClasses(): Flow<List<String>>
}