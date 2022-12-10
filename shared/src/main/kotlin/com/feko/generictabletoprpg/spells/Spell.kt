package com.feko.generictabletoprpg.spells

import com.feko.generictabletoprpg.common.Range
import com.feko.generictabletoprpg.import.ProcessEdnMapPort

data class Spell(
    val id: Long,
    val name: String,
    val description: String,
    val school: String,
    val duration: String,
    val concentration: Boolean,
    val level: Int,
    val source: String,
    val components: SpellComponents,
    val castingTime: String,
    val classesThatCanCast: List<String>,
    val range: Range
) {
    companion object {
        fun createFromOrcbrewData(
            processEdnMapPort: ProcessEdnMapPort,
            spellMap: Map<Any, Any>,
            componentsMap: Map<Any, Any>,
            classesThatCanCastMap: Map<Any, Any>,
            range: String,
            defaultSource: String
        ): Spell {
            val spellDuration =
                processEdnMapPort.getValueOrDefault(spellMap, ":duration", "Instantaneous")
            val classesThatCanCast =
                processEdnMapPort
                    .toStringKeyedMap<Boolean>(classesThatCanCastMap)
                    .filter { it.value }
                    .map { it.key.substring(1) }
            return Spell(
                0,
                processEdnMapPort.getValue(spellMap, ":name"),
                processEdnMapPort.getValue(spellMap, ":description"),
                processEdnMapPort.getValue(spellMap, ":school"),
                spellDuration,
                spellDuration.contains("Concentration"),
                processEdnMapPort.getValue(spellMap, ":level"),
                processEdnMapPort.getValueOrDefault(spellMap, ":source", defaultSource),
                SpellComponents.createFromOrcbrewData(processEdnMapPort, componentsMap),
                processEdnMapPort.getValue(spellMap, ":casting-time"),
                classesThatCanCast,
                Range.createFromString(range)
            )
        }
    }

    data class SpellComponents(
        val verbal: Boolean,
        val somatic: Boolean,
        val material: Boolean,
        val materialComponent: String?
    ) {
        companion object {
            fun createFromOrcbrewData(
                processEdnMapPort: ProcessEdnMapPort,
                componentsMap: Map<Any, Any>
            ): SpellComponents {
                return SpellComponents(
                    processEdnMapPort.getValueOrDefault(componentsMap, ":verbal", false),
                    processEdnMapPort.getValueOrDefault(componentsMap, ":somatic", false),
                    processEdnMapPort.getValueOrDefault(componentsMap, ":material", false),
                    processEdnMapPort.getValueOrDefault(componentsMap, ":material-component", "")
                )
            }
        }
    }
}

