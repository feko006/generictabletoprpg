package com.feko.generictabletoprpg.shared.features.io.domain.usecase

import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import com.feko.generictabletoprpg.shared.common.domain.IProcessEdnMap
import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition

//import timber.log.Timber

@Suppress("UNCHECKED_CAST")
class OrcbrewImportAmmunitionsUseCase(
    private val processEdnMapPort: IProcessEdnMap,
    private val insertAmmunitionsPort: IInsertAllDao<Ammunition>
) : IOrcbrewImportAmmunitionsUseCase {
    override suspend fun import(sources: Map<Any, Any>): Result<Boolean> {
        val ammunitionsToAdd = mutableListOf<Ammunition>()
        val exceptions = mutableListOf<Exception>()
        sources.forEach { source ->
            val content = source.value as Map<Any, Any>
            val ammunitionsKey = ":orcpub.dnd.e5/ammunitions"
            val hasValueForKey = processEdnMapPort.containsKey(content, ammunitionsKey)
            if (hasValueForKey) {
                val ammunitions = processEdnMapPort.getValue<Map<Any, Any>>(content, ammunitionsKey)
                ammunitions.forEach { ammunition ->
                    try {
                        val ammunitionMap = ammunition.value as Map<Any, Any>
                        val ammunitionToAdd = Ammunition.createFromOrcbrewData(
                            processEdnMapPort,
                            ammunitionMap,
                            source.key.toString()
                        )
                        ammunitionsToAdd.add(ammunitionToAdd)
                    } catch (e: Exception) {
                        // TODO
//                        Timber.e(e, "Failed to process ammunition named '${ammunition.key}.")
                        exceptions.add(e)
                    }
                }
            }
        }
        if (exceptions.isNotEmpty() and
            ammunitionsToAdd.isNotEmpty()
        ) {
            return Result.success(false)
        }
        if (ammunitionsToAdd.isEmpty()) {
            return Result.success(true)
        }
        return insertAmmunitionsPort.insertAll(ammunitionsToAdd)
    }
}