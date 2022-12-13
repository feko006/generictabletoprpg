package com.feko.generictabletoprpg.feat

import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.Named
import com.feko.generictabletoprpg.common.Stat
import com.feko.generictabletoprpg.import.ProcessEdnMapPort

data class Feat(
    override val id: Long,
    override val name: String,
    val description: String,
    val source: String,
    val abilityIncreases: List<Stat>,
    val proficiencyRequirements: List<String>,
    val statRequirements: List<Stat>,
    val raceRequirements: List<String>,
    val savingThrow: Boolean
) : Identifiable,
    Named {
    val hasRequirements: Boolean
        get() = proficiencyRequirements.isNotEmpty() or
                statRequirements.isNotEmpty() or
                raceRequirements.isNotEmpty()
    val requirements: String
        get() {
            val builder = StringBuilder()
            if (proficiencyRequirements.isNotEmpty()) {
                builder.append("\nProficiencies: ${proficiencyRequirements.joinToString()}")
            }
            if (statRequirements.isNotEmpty()) {
                builder.append("\nStats: ${statRequirements.joinToString { "${it.name} 13+" }}")
            }
            if (raceRequirements.isNotEmpty()) {
                builder.append("\nRace: ${raceRequirements.joinToString()}")
            }
            return builder.toString()
        }

    companion object {
        fun createFromOrcbrewData(
            processEdnMapPort: ProcessEdnMapPort,
            featMap: Map<Any, Any>,
            defaultSource: String
        ): Feat {
            val abilityIncreasesSet =
                processEdnMapPort.getValueOrDefault<Set<Any>>(
                    featMap,
                    ":ability-increases",
                    setOf()
                )
            val abilityIncreases = mutableListOf<Stat>()
            var savingThrow = false
            abilityIncreasesSet.forEach {
                val orcbrewString = it.toString()
                try {
                    val stat = Stat.fromOrcbrewString(orcbrewString)
                    abilityIncreases.add(stat)
                } catch (e: Exception) {
                    if (orcbrewString.contains("saves")) {
                        savingThrow = true
                    }
                }
            }
            val requirementsSet =
                processEdnMapPort.getValueOrDefault<Set<Any>>(
                    featMap,
                    ":prereqs",
                    setOf()
                )
            val proficiencyRequirements = mutableListOf<String>()
            val statRequirements = mutableListOf<Stat>()
            requirementsSet
                .map { it.toString() }
                .forEach {
                    try {
                        val stat = Stat.fromOrcbrewString(it)
                        statRequirements.add(stat)
                    } catch (e: Exception) {
                        proficiencyRequirements.add(it.substring(1))
                    }
                }
            val pathRequirementsMap =
                processEdnMapPort.getValueOrDefault<Map<Any, Any>>(
                    featMap,
                    ":path-prereqs",
                    mapOf()
                )
            val raceRequirementsMap =
                processEdnMapPort.getValueOrDefault<Map<Any, Boolean>>(
                    pathRequirementsMap,
                    ":race",
                    mapOf()
                )
            val raceRequirements =
                raceRequirementsMap
                    .filter { it.value }
                    .map {
                        it.key.toString().substring(1)
                    }
            return Feat(
                0,
                processEdnMapPort.getValue(featMap, ":name"),
                processEdnMapPort.getValue(featMap, ":description"),
                processEdnMapPort.getValueOrDefault(featMap, ":source", defaultSource),
                abilityIncreases,
                proficiencyRequirements,
                statRequirements,
                raceRequirements,
                savingThrow
            )
        }
    }
}