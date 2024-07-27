package com.feko.generictabletoprpg.spell

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.IGetById
import com.feko.generictabletoprpg.common.IInsertAll
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SpellDao
    : BaseDao<SpellEntity, Spell>(),
    IInsertAll<Spell>,
    IGetAll<Spell>,
    IGetById<Spell>,
    ISpellFilterDao {
    override fun getEntityFromCoreModel(item: Spell): SpellEntity =
        SpellEntity.fromCoreModel(item)

    @Query("select id from spells where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from spells order by name")
    abstract override fun getAllSortedByNameInternal(): List<SpellEntity>

    @Query("select * from spells where id = :id")
    abstract override fun getByIdInternal(id: Long): SpellEntity

    @Query("select distinct school from spells order by school asc")
    abstract override fun getAllSchools(): Flow<List<String>>

    @Query("select distinct level from spells order by level asc")
    abstract override fun getAllLevels(): Flow<List<Int>>

    @Query("select distinct classesThatCanCast from spells")
    abstract override fun getAllClasses(): Flow<List<String>>
}