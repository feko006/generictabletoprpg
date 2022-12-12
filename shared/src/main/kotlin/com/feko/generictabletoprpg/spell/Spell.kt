package com.feko.generictabletoprpg.spell

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
    val hasComponents: Boolean
        get() = components.any()

    companion object {
        fun createFromOrcbrewData(
            processEdnMapPort: ProcessEdnMapPort,
            spellMap: Map<Any, Any>,
            defaultSource: String
        ): Spell {
            val componentsMap =
                processEdnMapPort.getValueOrDefault(
                    spellMap,
                    ":components",
                    mapOf<Any, Any>()
                )
            val classesThatCanCastMap =
                processEdnMapPort.getValueOrDefault<Map<Any, Any>>(
                    spellMap,
                    ":spell-lists",
                    mapOf()
                )
            val rangeString =
                processEdnMapPort.getValue<String>(spellMap, ":range")
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
                processEdnMapPort
                    .getValueOrDefault<Any?>(
                        spellMap,
                        ":source",
                        null
                    )?.toString()
                    ?: defaultSource,
                SpellComponents.createFromOrcbrewData(processEdnMapPort, componentsMap),
                processEdnMapPort.getValue(spellMap, ":casting-time"),
                classesThatCanCast,
                Range.createFromString(rangeString)
            )
        }
    }

    data class SpellComponents(
        val verbal: Boolean,
        val somatic: Boolean,
        val material: Boolean,
        val materialComponent: String?
    ) {
        fun any(): Boolean =
            verbal or somatic or material

        override fun toString(): String {
            val builder = StringBuilder()
            if (verbal) {
                builder.append("V")
            }
            if (somatic) {
                if (verbal) {
                    builder.append(", ")
                }
                builder.append("S")
            }
            if (material) {
                if (verbal or somatic) {
                    builder.append(", ")
                }
                builder.append("M")
                materialComponent?.let {
                    builder.append(" ($it)")
                }
            }
            return builder.toString()
        }

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

