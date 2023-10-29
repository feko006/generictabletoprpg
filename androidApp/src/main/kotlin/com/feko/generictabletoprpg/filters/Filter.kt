package com.feko.generictabletoprpg.filters

import com.feko.generictabletoprpg.common.INamed
import com.feko.generictabletoprpg.spell.Spell

sealed class Filter(
    val type: Class<*>,
    val name: String? = null
) {
    open fun isAccepted(obj: Any): Boolean {
        var isAccepted = obj::class.java == type
        if (name != null) {
            isAccepted = isAccepted
                    && obj is INamed
                    && obj.name.lowercase().contains(name.lowercase())
        }
        return isAccepted
    }

    class GenericFilter(type: Class<*>, name: String? = null) : Filter(type, name)

    class SpellFilter(
        name: String? = null,
        val school: String? = null,
        val concentration: Boolean? = null,
        val level: Int? = null,
        val `class`: String? = null,
        val isRitual: Boolean? = null,
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
            concentration: Boolean? = null,
            level: Int? = null,
            `class`: String? = null,
            isRitual: Boolean? = null,
            spellComponents: Spell.SpellComponents? = null
        ): SpellFilter =
            SpellFilter(
                name,
                school ?: this.school,
                concentration ?: this.concentration,
                level ?: this.level,
                `class` ?: this.`class`,
                isRitual ?: this.isRitual,
                spellComponents ?: this.spellComponents
            )
    }
}
