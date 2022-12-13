package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.common.Logger

@Suppress("UNCHECKED_CAST")
class OrcbrewImportAllUseCaseImpl(
    private val parseEdnAsMapPort: ParseEdnAsMapPort,
    private val orcbrewImportSpellsUseCase: OrcbrewImportSpellsUseCase,
    private val orcbrewImportFeatsUseCase: OrcbrewImportFeatsUseCase,
    private val logger: Logger
) : OrcbrewImportAllUseCase {
    override fun import(ednContent: String): Result<Boolean> {
        try {
            val safeFileContents =
                if (ednContent[0] != '\ufeff') {
                    ednContent
                } else {
                    ednContent.substring(1)
                }
                    .replace("##NaN", "nil")
            val sources = parseEdnAsMapPort.parse(safeFileContents)
            val spellsImported = orcbrewImportSpellsUseCase.import(sources)
            val featsImported = orcbrewImportFeatsUseCase.import(sources)
            val everythingImported =
                spellsImported.getOrDefault(false) and
                        featsImported.getOrDefault(false)
            return Result.success(everythingImported)
        } catch (e: Exception) {
            logger.error(e, "Failed to process file")
            return Result.failure(e)
        }
    }
}