package com.feko.generictabletoprpg.shared.features.io.domain.usecase

import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import com.feko.generictabletoprpg.shared.common.domain.IProcessEdnMap
import com.feko.generictabletoprpg.shared.features.armor.Armor
import com.feko.generictabletoprpg.shared.logger

@Suppress("UNCHECKED_CAST")
class OrcbrewImportArmorsUseCase(
    private val processEdnMapPort: IProcessEdnMap,
    private val insertArmorsPort: IInsertAllDao<Armor>
) : IOrcbrewImportArmorsUseCase {
    override suspend fun import(sources: Map<Any, Any>): Result<Pair<Int, Int>> {
        val armorsToAdd = mutableListOf<Armor>()
        val exceptions = mutableListOf<Throwable>()
        sources.forEach { source ->
            val content = source.value as? Map<Any, Any>
            val armorsKey = ":orcpub.dnd.e5/armors"
            val hasValueForKey = processEdnMapPort.containsKey(content, armorsKey)
            if (hasValueForKey) {
                val armors = processEdnMapPort.getValue<Map<Any, Any>>(content, armorsKey)
                armors.forEach { armor ->
                    try {
                        val armorMap = armor.value as? Map<Any, Any>
                        val armorToAdd = Armor.createFromOrcbrewData(
                            processEdnMapPort,
                            armorMap,
                            source.key.toString()
                        )
                        armorsToAdd.add(armorToAdd)
                    } catch (e: Throwable) {
                        logger.error(e) { "Failed to process armor named '${armor.key}." }
                        exceptions.add(e)
                    }
                }
            }
        }
        if (exceptions.isNotEmpty() and armorsToAdd.isNotEmpty()) {
            return Result.failure(exceptions.first())
        }
        return insertArmorsPort.insertAll(armorsToAdd)
    }
}