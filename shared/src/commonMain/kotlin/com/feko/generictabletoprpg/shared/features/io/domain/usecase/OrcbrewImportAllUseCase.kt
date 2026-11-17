package com.feko.generictabletoprpg.shared.features.io.domain.usecase

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.ammunition
import com.feko.generictabletoprpg.armor
import com.feko.generictabletoprpg.feat
import com.feko.generictabletoprpg.shared.common.domain.IParseEdnAsMap
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.logger
import com.feko.generictabletoprpg.spell
import com.feko.generictabletoprpg.weapon

class OrcbrewImportAllUseCase(
    private val parseEdnAsMapPort: IParseEdnAsMap,
    private val orcbrewImportSpellsUseCase: IOrcbrewImportSpellsUseCase,
    private val orcbrewImportFeatsUseCase: IOrcbrewImportFeatsUseCase,
    private val orcbrewImportWeaponsUseCase: IOrcbrewImportWeaponsUseCase,
    private val orcbrewImportAmmunitionsUseCase: IOrcbrewImportAmmunitionsUseCase,
    private val orcbrewImportArmorsUseCase: IOrcbrewImportArmorsUseCase
) : IOrcbrewImportAllUseCase {
    override suspend fun import(content: String): Result<List<ImportResult>> {
        try {
            val safeFileContents =
                if (content[0] != '\ufeff') {
                    content
                } else {
                    content.substring(1)
                }
                    .replace("##NaN", "nil")
            var sources = parseEdnAsMapPort.parse(safeFileContents)
            requireNotNull(sources)
            if (sources.keys.any { it.toString().contains("orcpub") }) {
                sources = mapOf("" to sources)
            }
            val results = mutableListOf<ImportResult>()
            val spellsImported = orcbrewImportSpellsUseCase.import(sources)
            spellsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.spell.asText(), it.first, it.second))
            }
            val featsImported = orcbrewImportFeatsUseCase.import(sources)
            featsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.feat.asText(), it.first, it.second))
            }
            val weaponsImported = orcbrewImportWeaponsUseCase.import(sources)
            weaponsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.weapon.asText(), it.first, it.second))
            }
            val ammunitionsImported = orcbrewImportAmmunitionsUseCase.import(sources)
            ammunitionsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.ammunition.asText(), it.first, it.second))
            }
            val armorsImported = orcbrewImportArmorsUseCase.import(sources)
            armorsImported.getOrNull()?.let {
                results.add(ImportResult(Res.string.armor.asText(), it.first, it.second))
            }
            return Result.success(results)
        } catch (e: Throwable) {
            logger.error(e) { "Failed to process file" }
            return Result.failure(e)
        }
    }
}

