package com.feko.generictabletoprpg.shared.features.io.domain.usecase

import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import com.feko.generictabletoprpg.shared.common.domain.IProcessEdnMap
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.logger

@Suppress("UNCHECKED_CAST")
class OrcbrewImportSpellsUseCase(
    private val processEdnMapPort: IProcessEdnMap,
    private val insertSpellsPort: IInsertAllDao<Spell>
) : IOrcbrewImportSpellsUseCase {
    override suspend fun import(sources: Map<Any, Any>): Result<Pair<Int, Int>> {
        val spellsToAdd = mutableListOf<Spell>()
        val exceptions = mutableListOf<Throwable>()
        sources.forEach { source ->
            val content = source.value as? Map<Any, Any>
            val spellsKey = ":orcpub.dnd.e5/spells"
            val hasValueForKey = processEdnMapPort.containsKey(content, spellsKey)
            if (hasValueForKey) {
                val spells = processEdnMapPort.getValue<Map<Any, Any>>(content, spellsKey)
                spells.forEach { spell ->
                    try {
                        val spellMap = spell.value as? Map<Any, Any>
                        val spellToAdd = Spell.createFromOrcbrewData(
                            processEdnMapPort,
                            spellMap,
                            source.key.toString()
                        )
                        spellsToAdd.add(spellToAdd)
                    } catch (e: Throwable) {
                        logger.error(e) { "Failed to process spell named '${spell.key}." }
                        exceptions.add(e)
                    }
                }
            }
        }
        if (exceptions.isNotEmpty() and spellsToAdd.isNotEmpty()) {
            return Result.failure(exceptions.first())
        }
        return insertSpellsPort.insertAll(spellsToAdd)
    }
}