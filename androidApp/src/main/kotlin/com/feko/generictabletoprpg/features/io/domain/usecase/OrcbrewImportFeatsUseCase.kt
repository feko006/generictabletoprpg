package com.feko.generictabletoprpg.features.io.domain.usecase

import com.feko.generictabletoprpg.common.data.local.IInsertAllDao
import com.feko.generictabletoprpg.features.feat.Feat
import com.feko.generictabletoprpg.common.domain.IProcessEdnMap
import timber.log.Timber

@Suppress("UNCHECKED_CAST")
class OrcbrewImportFeatsUseCase(
    private val processEdnMapPort: IProcessEdnMap,
    private val insertFeatsPort: IInsertAllDao<Feat>
) : IOrcbrewImportFeatsUseCase {
    override fun import(
        sources: Map<Any, Any>
    ): Result<Boolean> {
        val featsToAdd = mutableListOf<Feat>()
        val exceptions = mutableListOf<Exception>()
        sources.forEach { source ->
            val content = source.value as Map<Any, Any>
            val featsKey = ":orcpub.dnd.e5/feats"
            val hasValueForKey = processEdnMapPort.containsKey(content, featsKey)
            if (hasValueForKey) {
                val feats = processEdnMapPort.getValue<Map<Any, Any>>(content, featsKey)
                feats.forEach { feat ->
                    try {
                        val featMap = feat.value as Map<Any, Any>
                        val featToAdd = Feat.createFromOrcbrewData(
                            processEdnMapPort,
                            featMap,
                            source.key.toString()
                        )
                        featsToAdd.add(featToAdd)
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to process feat named '${feat.key}.")
                        exceptions.add(e)
                    }
                }
            }
        }
        if (exceptions.isNotEmpty() and
            featsToAdd.isNotEmpty()
        ) {
            return Result.success(false)
        }
        if (featsToAdd.isEmpty()) {
            return Result.success(true)
        }
        return insertFeatsPort.insertAll(featsToAdd)
    }
}