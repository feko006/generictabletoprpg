package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep
import com.feko.generictabletoprpg.common.domain.model.IFromSource
import com.feko.generictabletoprpg.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed
import com.feko.generictabletoprpg.features.spell.Spell
import com.feko.generictabletoprpg.features.spell.SpellRange

@Keep
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

        val Empty = SpellListEntry(
            0L,
            "",
            "",
            "",
            "",
            false,
            0,
            "",
            Spell.SpellComponents.Empty,
            "",
            emptyList(),
            SpellRange.Empty,
            false,
            false
        )
    }

    fun toSpell() =
        Spell(
            id, name, description, school, duration, concentration, level, source,
            components, castingTime, classesThatCanCast, range, isRitual
        )

}

fun List<SpellListEntry>.preparedSpellsCount() = count { it.isPrepared }

fun List<SpellListEntry>.cantripSpellsCount() = count { it.level == 0 }

fun List<SpellListEntry>.filterPreparedAndCantrips(preparedSpellsOnly: Boolean) =
    if (preparedSpellsOnly) filter { it.isPrepared || it.level == 0 }
    else this

