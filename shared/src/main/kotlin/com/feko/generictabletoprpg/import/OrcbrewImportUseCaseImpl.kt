package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.spells.Spell

class OrcbrewImportUseCaseImpl(
    val parseEdnAsMapPort: ParseEdnAsMapPort,
    val processEdnMapPort: ProcessEdnMapPort,
    val saveSpellsPort: SaveSpellsPort,
    val logger: Logger
) : OrcbrewImportUseCase {
    override fun invoke(fileContents: String): Result<Boolean> {
        try {
            val safeFileContents =
                if (fileContents[0] != '\ufeff') {
                    fileContents
                } else {
                    fileContents.substring(1)
                }
                    .replace("##NaN", "nil")
            val sources = parseEdnAsMapPort.invoke(safeFileContents)
            val spellsToAdd = mutableListOf<Spell>()
            sources.forEach { source ->
                @Suppress("UNCHECKED_CAST")
                val content = source.value as Map<Any, Any>
                val spellsKey = ":orcpub.dnd.e5/spells"
                val hasValueForKey = processEdnMapPort.containsKey(content, spellsKey)
                if (hasValueForKey) {
                    @Suppress("UNCHECKED_CAST")
                    val spells = processEdnMapPort.getValue<Map<Any, Any>>(content, spellsKey)
                    spells.forEach { spell ->
                        try {
                            @Suppress("UNCHECKED_CAST")
                            val spellMap = spell.value as Map<Any, Any>

                            @Suppress("UNCHECKED_CAST")
                            val componentsMap =
                                processEdnMapPort.getValueOrDefault(
                                    spellMap,
                                    ":components",
                                    mapOf<Any, Any>()
                                )

                            @Suppress("UNCHECKED_CAST")
                            val classesThatCanCastMap =
                                processEdnMapPort.getValue<Map<Any, Any>>(spellMap, ":spell-lists")

                            val rangeString =
                                processEdnMapPort.getValue<String>(spellMap, ":range")

                            val spellToAdd = Spell.createFromOrcbrewData(
                                processEdnMapPort,
                                spellMap,
                                componentsMap,
                                classesThatCanCastMap,
                                rangeString,
                                source.key.toString()
                            )
                            spellsToAdd.add(spellToAdd)
                        } catch (e: Exception) {
                            logger.error(e, "Failed to process spell named '${spell.key}.")
                        }
                    }
                }
            }
            if (spellsToAdd.isEmpty()) {
                return Result.success(false)
            }
            return saveSpellsPort.save(spellsToAdd)
        } catch (e: Exception) {
            logger.error(e, "Failed to process file")
            return Result.failure(e)
        }
    }
}