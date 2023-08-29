package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.common.IInsertAll
import com.feko.generictabletoprpg.common.Logger
import com.feko.generictabletoprpg.weapon.Weapon

@Suppress("UNCHECKED_CAST")
class OrcbrewImportWeaponsUseCaseImpl(
    private val processEdnMapPort: IProcessEdnMap,
    private val insertWeaponsPort: IInsertAll<Weapon>,
    private val logger: Logger
) : OrcbrewImportWeaponsUseCase {
    override fun import(
        sources: Map<Any, Any>
    ): Result<Boolean> {
        val weaponsToAdd = mutableListOf<Weapon>()
        val exceptions = mutableListOf<Exception>()
        sources.forEach { source ->
            val content = source.value as Map<Any, Any>
            val weaponsKey = ":orcpub.dnd.e5/weapons"
            val hasValueForKey = processEdnMapPort.containsKey(content, weaponsKey)
            if (hasValueForKey) {
                val weapons = processEdnMapPort.getValue<Map<Any, Any>>(content, weaponsKey)
                weapons.forEach { weapon ->
                    try {
                        val weaponMap = weapon.value as Map<Any, Any>
                        val weaponToAdd = Weapon.createFromOrcbrewData(
                            processEdnMapPort,
                            weaponMap,
                            source.key.toString()
                        )
                        weaponsToAdd.add(weaponToAdd)
                    } catch (e: Exception) {
                        logger.error(e, "Failed to process weapon named '${weapon.key}.")
                        exceptions.add(e)
                    }
                }
            }
        }
        if (exceptions.isNotEmpty() and
            weaponsToAdd.isNotEmpty()
        ) {
            return Result.success(false)
        }
        if (weaponsToAdd.isEmpty()) {
            return Result.success(true)
        }
        return insertWeaponsPort.insertAll(weaponsToAdd)
    }
}