package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.IFromSource
import com.feko.generictabletoprpg.common.IIdentifiable
import com.feko.generictabletoprpg.common.INamed
import com.feko.generictabletoprpg.spell.Spell
import com.feko.generictabletoprpg.spell.SpellRange

@DoNotObfuscate
data class SpellListEntry(
    override val id: Long = 0,
    override val name: String,
    val description: String,
    val school: String,
    val duration: String,
    val concentration: Boolean,
    val level: Int,
    override val source: String,
    val components: Spell.SpellComponents,
    val castingTime: String,
    val classesThatCanCast: List<String>,
    val range: SpellRange,
    val isRitual: Boolean,
    var isPrepared: Boolean = false
) : IIdentifiable,
    INamed,
    IFromSource {

    companion object {
        fun fromSpell(spell: Spell) =
            spell.run {
                SpellListEntry(
                    id, name, description, school, duration, concentration, level, source,
                    components, castingTime, classesThatCanCast, range, isRitual, isPrepared = false
                )
            }
    }

    fun toSpell() =
        Spell(
            id, name, description, school, duration, concentration, level, source,
            components, castingTime, classesThatCanCast, range, isRitual
        )

}

fun List<SpellListEntry>.containsPreparedSpells() = any { it.isPrepared || it.level == 0 }

fun List<SpellListEntry>.preparedSpellsCount() = count { it.isPrepared || it.level == 0 }

fun List<SpellListEntry>.filterPrepared(preparedSpellsOnly: Boolean) =
    if (preparedSpellsOnly) filter { it.isPrepared || it.level == 0 }
    else this

