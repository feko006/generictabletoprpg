package com.feko.generictabletoprpg.filters

import com.feko.generictabletoprpg.spell.Spell

class SpellFilter(
    name: String? = null,
    val school: String? = null,
    var concentration: Boolean? = null,
    val level: Int? = null,
    val `class`: String? = null,
    var isRitual: Boolean? = null,
    val spellComponents: Spell.SpellComponents? = null
) : Filter(Spell::class.java, name) {
    override fun isAccepted(obj: Any): Boolean {
        var isAccepted = super.isAccepted(obj)
        val spell = obj as Spell
        school?.let { isAccepted = isAccepted && spell.school == it }
        concentration?.let { isAccepted = isAccepted && spell.concentration == it }
        level?.let { isAccepted = isAccepted && spell.level == it }
        `class`?.let { isAccepted = isAccepted && spell.classesThatCanCast.contains(it) }
        isRitual?.let { isAccepted = isAccepted && spell.isRitual == it }
        spellComponents?.let {
            isAccepted = isAccepted
                    && spell.hasComponents
                    && spell.components.material == it.material
                    && spell.components.somatic == it.somatic
                    && spell.components.verbal == it.verbal
        }
        return isAccepted
    }

    fun copy(
        school: String? = null,
        level: Int? = null,
        `class`: String? = null,
        isRitual: Boolean? = null,
        spellComponents: Spell.SpellComponents? = null
    ) = SpellFilter(
        name,
        school ?: this.school,
        concentration,
        level ?: this.level,
        `class` ?: this.`class`,
        isRitual ?: this.isRitual,
        spellComponents ?: this.spellComponents
    )

    fun copy(concentration: Boolean? = null) =
        SpellFilter(
            name,
            school,
            concentration,
            level,
            `class`,
            isRitual,
            spellComponents
        )
}
