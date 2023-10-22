package com.feko.generictabletoprpg.import

import timber.log.Timber

class OrcbrewImportAllUseCase(
    private val parseEdnAsMapPort: IParseEdnAsMap,
    private val orcbrewImportSpellsUseCase: IOrcbrewImportSpellsUseCase,
    private val orcbrewImportFeatsUseCase: IOrcbrewImportFeatsUseCase,
    private val orcbrewImportWeaponsUseCase: IOrcbrewImportWeaponsUseCase,
    private val orcbrewImportAmmunitionsUseCase: IOrcbrewImportAmmunitionsUseCase,
    private val orcbrewImportArmorsUseCase: IOrcbrewImportArmorsUseCase
) : IOrcbrewImportAllUseCase {
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
            val weaponsImported = orcbrewImportWeaponsUseCase.import(sources)
            results.add(weaponsImported)
            val ammunitionsImported = orcbrewImportAmmunitionsUseCase.import(sources)
            results.add(ammunitionsImported)
            val armorsImported = orcbrewImportArmorsUseCase.import(sources)
            results.add(armorsImported)
            val everythingImported =
                results.fold(true) { current, result ->
                    current && result.getOrDefault(false)
                }
            return Result.success(everythingImported)
        } catch (e: Exception) {
            Timber.e(e, "Failed to process file")
            return Result.failure(e)
        }
    }
}

