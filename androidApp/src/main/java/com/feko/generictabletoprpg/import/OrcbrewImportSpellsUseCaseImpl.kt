package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.common.IInsertAll
import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.spell.Spell

@Suppress("UNCHECKED_CAST")
class OrcbrewImportSpellsUseCaseImpl(
    private val processEdnMapPort: IProcessEdnMap,
    private val insertSpellsPort: IInsertAll<Spell>,
    private val logger: Logger
) : OrcbrewImportSpellsUseCase {
    override fun import(
        sources: Map<Any, Any>
    ): Result<Boolean> {
        val spellsToAdd = mutableListOf<Spell>()
        val exceptions = mutableListOf<Exception>()
        sources.forEach { source ->
            val content = source.value as Map<Any, Any>
            val spellsKey = ":orcpub.dnd.e5/spells"
            val hasValueForKey = processEdnMapPort.containsKey(content, spellsKey)
            if (hasValueForKey) {
                val spells = processEdnMapPort.getValue<Map<Any, Any>>(content, spellsKey)
                spells.forEach { spell ->
                    try {
                        val spellMap = spell.value as Map<Any, Any>
                        val spellToAdd = Spell.createFromOrcbrewData(
                            processEdnMapPort,
                            spellMap,
                            source.key.toString()
                        )
                        spellsToAdd.add(spellToAdd)
                    } catch (e: Exception) {
                        logger.error(e, "Failed to process spell named '${spell.key}.")
                        exceptions.add(e)
                    }
                }
            }
        }
        if (exceptions.isNotEmpty() and
            spellsToAdd.isNotEmpty()
        ) {
            return Result.success(false)
        }
        if (spellsToAdd.isEmpty()) {
            return Result.success(true)
        }
        return insertSpellsPort.insertAll(spellsToAdd)
    }
}