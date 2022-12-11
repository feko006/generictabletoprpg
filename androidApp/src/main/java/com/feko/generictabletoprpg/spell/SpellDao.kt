package com.feko.generictabletoprpg.spell

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.import.SaveSpellsPort
import com.feko.generictabletoprpg.spells.Spell

@Dao
abstract class SpellDao
    : BaseDao<SpellEntity>(),
    SaveSpellsPort {

    lateinit var logger: Logger

    override fun save(spells: List<Spell>): Result<Boolean> {
        val errors = mutableListOf<Exception>()
        spells.forEach { spell ->
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
}