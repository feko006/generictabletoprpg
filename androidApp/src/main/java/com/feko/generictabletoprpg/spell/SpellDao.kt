package com.feko.generictabletoprpg.spell

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao

@Dao
abstract class SpellDao
    : BaseDao<SpellEntity, Spell>(),
    InsertSpellsPort,
    GetAllSpellsPort,
    GetSpellByIdPort {
    override fun getEntityFromCoreModel(item: Spell): SpellEntity =
        SpellEntity.fromCoreModel(item)

    @Query("select id from spells where name = :name")
    abstract override fun getEntityIdByName(name: String): Long?

    @Query("select * from spells order by name")
    abstract override fun getAllSortedByNameInternal(): List<SpellEntity>

    @Query("select * from spells where id = :id")
    abstract override fun getByIdInternal(id: Long): SpellEntity
}