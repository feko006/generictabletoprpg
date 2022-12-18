package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.common.Logger

@Suppress("UNCHECKED_CAST")
class OrcbrewImportAllUseCaseImpl(
    private val parseEdnAsMapPort: ParseEdnAsMapPort,
    private val orcbrewImportSpellsUseCase: OrcbrewImportSpellsUseCase,
    private val orcbrewImportFeatsUseCase: OrcbrewImportFeatsUseCase,
    private val logger: Logger
) : OrcbrewImportAllUseCase {
    override fun import(content: String): Result<Boolean> {
        try {
            val safeFileContents =
                if (content[0] != '\ufeff') {
                    content
                } else {
                    content.substring(1)
                }
                    .replace("##NaN", "nil")
            val sources = parseEdnAsMapPort.parse(safeFileContents)
            val results = mutableListOf<Result<Boolean>>()
            val spellsImported = orcbrewImportSpellsUseCase.import(sources)
            results.add(spellsImported)
            val featsImported = orcbrewImportFeatsUseCase.import(sources)
            results.add(featsImported)
            val everythingImported =
                results.fold(true) { current, result ->
                    current && result.getOrDefault(false)
                }
            return Result.success(everythingImported)
        } catch (e: Exception) {
            logger.error(e, "Failed to process file")
            return Result.failure(e)
        }
    }
}