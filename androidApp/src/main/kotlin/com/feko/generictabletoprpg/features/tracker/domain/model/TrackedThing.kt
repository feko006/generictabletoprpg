package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Keep
@Serializable
data class TrackedThing(
    @Transient
    override var id: Long = 0L,
    override var name: String,
    var value: String = "",
    val type: Type,
    var index: Int = 0,
    @Transient
    var groupId: Long = 0L,
    var temporaryHp: Int = 0,
    var level: Int = 0,
    private var defaultValue: String = type.initialDefaultValue,
    @Transient
    @Contextual
    var serializedItem: Any = 0
) : IMutableIdentifiable,
    INamed {

    var managedDefaultValue: String
        get() = defaultValue
        set(value) {
            defaultValue = type.normalize(value)
        }

    @Keep
    enum class Type(@StringRes val nameResource: Int) {
        None(0),
        Percentage(R.string.percentage),
        Health(R.string.health),
        Ability(R.string.ability),
        SpellSlot(R.string.spell_slot),
        Number(R.string.number),
        SpellList(R.string.spell_list),
        Text(R.string.text),
        HitDice(R.string.hit_dice),
        FiveEStats(R.string.five_e_stats)
    }

    companion object {
        fun emptyOfType(type: Type, index: Int, groupId: Long): TrackedThing {
            val empty = TrackedThing(name = "", type = type, index = index, groupId = groupId)
            if (type == Type.Number) {
                empty.managedDefaultValue = Int.MAX_VALUE.toString()
            }
            if (type == Type.SpellSlot) {
                empty.level = 1
            }
            if (type == Type.SpellList) {
                empty.value = "[]"
                empty.serializedItem = empty.getItem()
            }
            if (type == Type.FiveEStats) {
                empty.setItem(StatsContainer.Empty)
            }
            return empty
        }
    }
}