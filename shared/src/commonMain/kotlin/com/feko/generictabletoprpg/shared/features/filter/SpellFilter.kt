package com.feko.generictabletoprpg.shared.features.filter

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.class_term
import com.feko.generictabletoprpg.concentration
import com.feko.generictabletoprpg.level
import com.feko.generictabletoprpg.material
import com.feko.generictabletoprpg.no
import com.feko.generictabletoprpg.ritual
import com.feko.generictabletoprpg.school
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringText.Companion.asText
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.somatic
import com.feko.generictabletoprpg.verbal
import com.feko.generictabletoprpg.yes

class SpellFilter(
    name: String? = null,
    val schools: List<String> = emptyList(),
    var concentration: Boolean? = null,
    val levels: List<Int> = emptyList(),
    val classes: List<String> = emptyList(),
    var isRitual: Boolean? = null,
    val spellComponents: SpellComponentsFilter = SpellComponentsFilter()
) : Filter(Spell::class.java, name) {

    override fun isAccepted(obj: Any): Boolean {
        var isAccepted = super.isAccepted(obj)
        if (!isAccepted) return false

        val spell = obj as Spell
        isAccepted = schools.isEmpty() || schools.any { it.equals(spell.school, ignoreCase = true) }
        concentration?.let { isAccepted = isAccepted && spell.concentration == it }
        isAccepted = isAccepted && levels.let { it.isEmpty() || it.any { it == spell.level } }
        isAccepted = isAccepted && classes.let {
            it.isEmpty() || it.any { filterClass ->
                spell.classesThatCanCast.any { classThatCanCast ->
                    classThatCanCast.equals(filterClass, ignoreCase = true)
                }
            }
        }
        isRitual?.let { isAccepted = isAccepted && spell.isRitual == it }
        spellComponents.let {
            it.verbal?.let { isAccepted = isAccepted && spell.components.verbal == it }
            it.somatic?.let { isAccepted = isAccepted && spell.components.somatic == it }
            it.material?.let { isAccepted = isAccepted && spell.components.material == it }
        }
        return isAccepted
    }

    fun copy(
        schools: List<String>? = null,
        levels: List<Int>? = null,
        classes: List<String>? = null
    ) = SpellFilter(
        name,
        schools ?: this.schools,
        concentration,
        levels ?: this.levels,
        classes ?: this.classes,
        isRitual,
        spellComponents
    )

    fun copyWithNewConcentration(concentration: Boolean? = null) =
        SpellFilter(
            name,
            schools,
            concentration,
            levels,
            classes,
            isRitual,
            spellComponents
        )

    fun copyWithNewRitual(isRitual: Boolean? = null) =
        SpellFilter(
            name,
            schools,
            concentration,
            levels,
            classes,
            isRitual,
            spellComponents
        )

    fun copyWithNewSpellComponents(
        spellComponents: SpellComponentsFilter
    ) = SpellFilter(
        name,
        schools,
        concentration,
        levels,
        classes,
        isRitual,
        spellComponents
    )

    override val chipData: List<FilterChipData>
        get() = buildList {
            addAll(super.chipData)
            schools.forEach {
                add(
                    FilterChipData(
                        Res.string.school.asText(),
                        it.asText(),
                        copy(schools = schools.minus(it))
                    )
                )
            }
            concentration?.let {
                add(
                    FilterChipData(
                        Res.string.concentration.asText(),
                        if (it) Res.string.yes.asText() else Res.string.no.asText(),
                        copyWithNewConcentration(null)
                    )
                )
            }
            levels.forEach {
                add(
                    FilterChipData(
                        Res.string.level.asText(),
                        it.toString().asText(),
                        copy(levels = levels.minus(it))
                    )
                )
            }
            classes.forEach {
                add(
                    FilterChipData(
                        Res.string.class_term.asText(),
                        it.asText(),
                        copy(classes = classes.minus(it))
                    )
                )
            }
            isRitual?.let {
                add(
                    FilterChipData(
                        Res.string.ritual.asText(),
                        if (it) Res.string.yes.asText() else Res.string.no.asText(),
                        copyWithNewRitual(null)
                    )
                )
            }
            spellComponents.let {
                if (it.verbal != null) {
                    add(
                        FilterChipData(
                            Res.string.verbal.asText(),
                            if (it.verbal) Res.string.yes.asText() else Res.string.no.asText(),
                            copyWithNewSpellComponents(it.copy(verbal = null))
                        )
                    )
                }
                if (it.somatic != null) {
                    add(
                        FilterChipData(
                            Res.string.somatic.asText(),
                            if (it.somatic) Res.string.yes.asText() else Res.string.no.asText(),
                            copyWithNewSpellComponents(it.copy(somatic = null))
                        )
                    )
                }
                if (it.material != null) {
                    add(
                        FilterChipData(
                            Res.string.material.asText(),
                            if (it.material) Res.string.yes.asText() else Res.string.no.asText(),
                            copyWithNewSpellComponents(it.copy(material = null))
                        )
                    )
                }
            }
        }
}

data class SpellComponentsFilter(
    val verbal: Boolean? = null,
    val somatic: Boolean? = null,
    val material: Boolean? = null
)
