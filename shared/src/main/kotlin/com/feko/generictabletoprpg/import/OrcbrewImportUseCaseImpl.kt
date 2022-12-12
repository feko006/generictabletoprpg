package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.common.Logger

@Suppress("UNCHECKED_CAST")
class OrcbrewImportUseCaseImpl(
    private val parseEdnAsMapPort: ParseEdnAsMapPort,
    private val orcbrewImportSpellsUseCase: OrcbrewImportSpellsUseCase,
    private val orcbrewImportFeatsUseCase: OrcbrewImportFeatsUseCase,
    private val logger: Logger
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
            val spellsImported = orcbrewImportSpellsUseCase.import(sources)
            val featsImported = orcbrewImportFeatsUseCase.import(sources)
            val everythingImported =
                spellsImported.getOrDefault(false)
                        && featsImported.getOrDefault(false)
            return Result.success(everythingImported)
        } catch (e: Exception) {
            logger.error(e, "Failed to process file")
            return Result.failure(e)
        }
    }
}