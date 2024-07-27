package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.armor.Armor
import com.feko.generictabletoprpg.common.IInsertAll
import timber.log.Timber

@Suppress("UNCHECKED_CAST")
class OrcbrewImportArmorsUseCase(
    private val processEdnMapPort: IProcessEdnMap,
    private val insertArmorsPort: IInsertAll<Armor>

) : IOrcbrewImportArmorsUseCase {
    override fun import(sources: Map<Any, Any>): Result<Boolean> {
        val armorsToAdd = mutableListOf<Armor>()
        val exceptions = mutableListOf<Exception>()
        sources.forEach { source ->
            val content = source.value as Map<Any, Any>
            val armorsKey = ":orcpub.dnd.e5/armors"
            val hasValueForKey = processEdnMapPort.containsKey(content, armorsKey)
            if (hasValueForKey) {
                val armors = processEdnMapPort.getValue<Map<Any, Any>>(content, armorsKey)
                armors.forEach { armor ->
                    try {
                        val armorMap = armor.value as Map<Any, Any>
                        val armorToAdd = Armor.createFromOrcbrewData(
                            processEdnMapPort,
                            armorMap,
                            source.key.toString()
                        )
                        armorsToAdd.add(armorToAdd)
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to process armor named '${armor.key}.")
                        exceptions.add(e)
                    }
                }
            }
        }
        if (exceptions.isNotEmpty() and
            armorsToAdd.isNotEmpty()
        ) {
            return Result.success(false)
        }
        if (armorsToAdd.isEmpty()) {
            return Result.success(true)
        }
        return insertArmorsPort.insertAll(armorsToAdd)
    }
}