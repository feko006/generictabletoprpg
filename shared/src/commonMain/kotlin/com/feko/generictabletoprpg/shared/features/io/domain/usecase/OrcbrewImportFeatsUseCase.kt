package com.feko.generictabletoprpg.shared.features.io.domain.usecase

import com.feko.generictabletoprpg.shared.common.data.local.IInsertAllDao
import com.feko.generictabletoprpg.shared.common.domain.IProcessEdnMap
import com.feko.generictabletoprpg.shared.features.feat.Feat
import com.feko.generictabletoprpg.shared.logger

@Suppress("UNCHECKED_CAST")
class OrcbrewImportFeatsUseCase(
    private val processEdnMapPort: IProcessEdnMap,
    private val insertFeatsPort: IInsertAllDao<Feat>
) : IOrcbrewImportFeatsUseCase {
    override suspend fun import(sources: Map<Any, Any>): Result<Pair<Int, Int>> {
        val featsToAdd = mutableListOf<Feat>()
        val exceptions = mutableListOf<Throwable>()
        sources.forEach { source ->
            val content = source.value as? Map<Any, Any>
            val featsKey = ":orcpub.dnd.e5/feats"
            val hasValueForKey = processEdnMapPort.containsKey(content, featsKey)
            if (hasValueForKey) {
                val feats = processEdnMapPort.getValue<Map<Any, Any>>(content, featsKey)
                feats.forEach { feat ->
                    try {
                        val featMap = feat.value as? Map<Any, Any>
                        val featToAdd = Feat.createFromOrcbrewData(
                            processEdnMapPort,
                            featMap,
                            source.key.toString()
                        )
                        featsToAdd.add(featToAdd)
                    } catch (e: Throwable) {
                        logger.error(e) { "Failed to process feat named '${feat.key}." }
                        exceptions.add(e)
                    }
                }
            }
        }
        if (exceptions.isNotEmpty() and featsToAdd.isNotEmpty()) {
            return Result.failure(exceptions.first())
        }
        return insertFeatsPort.insertAll(featsToAdd)
    }
}