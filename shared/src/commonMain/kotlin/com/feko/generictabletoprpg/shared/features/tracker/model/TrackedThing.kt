package com.feko.generictabletoprpg.shared.features.tracker.model

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.ability
import com.feko.generictabletoprpg.five_e_stats
import com.feko.generictabletoprpg.health
import com.feko.generictabletoprpg.hit_dice
import com.feko.generictabletoprpg.number
import com.feko.generictabletoprpg.percentage
import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.spell_list
import com.feko.generictabletoprpg.spell_slot
import com.feko.generictabletoprpg.text
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.compose.resources.StringResource

@DoNotObfuscate
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

    @DoNotObfuscate
    enum class Type(val nameResource: StringResource?) {
        None(null),
        Percentage(Res.string.percentage),
        Health(Res.string.health),
        Ability(Res.string.ability),
        SpellSlot(Res.string.spell_slot),
        Number(Res.string.number),
        SpellList(Res.string.spell_list),
        Text(Res.string.text),
        HitDice(Res.string.hit_dice),
        FiveEStats(Res.string.five_e_stats)
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