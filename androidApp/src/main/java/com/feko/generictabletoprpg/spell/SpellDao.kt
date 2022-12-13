package com.feko.generictabletoprpg.spell

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.Logger

@Dao
abstract class SpellDao
    : BaseDao<SpellEntity>(),
    InsertSpellsPort,
    GetAllSpellsPort,
    GetSpellByIdPort {

    lateinit var logger: Logger

    override fun insert(list: List<Spell>): Result<Boolean> {
        val errors = mutableListOf<Exception>()
        list.forEach { spell ->
            try {
                val spellEntity = SpellEntity.fromCoreModel(spell)
                insertOrUpdate(spellEntity)
            } catch (e: Exception) {
                logger.error(e, "Saving spell with name ${spell.name} failed.")
                errors.add(e)
            }
        }
        return Result.success(errors.isEmpty())
    }

    @Query("select id from spells where name = :spellName")
    protected abstract fun getEntityIdByName(spellName: String): Long?

    override fun getEntityId(entity: SpellEntity): Long? = getEntityIdByName(entity.name)

    override fun setEntityId(entity: SpellEntity, existingEntityId: Long) {
        entity.id = existingEntityId
    }

    @Query("select * from spells order by name")
    protected abstract fun getAllSortedByNameInternal(): List<SpellEntity>

    override fun getAllSortedByName(): List<Spell> =
        getAllSortedByNameInternal()
            .map { it.toCoreModel() }

    @Query("select * from spells where id = :spellId")
    protected abstract fun getByIdInternal(spellId: Long): SpellEntity

    override fun getById(id: Long): Spell =
        getByIdInternal(id).toCoreModel()
}