package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed

@Keep
sealed class TrackedThing(
    @Transient
    override var id: Long = 0L,
    override var name: String,
    var value: String = "",
    val type: Type,
    var index: Int = 0,
    @Transient
    var groupId: Long = 0L
) : IMutableIdentifiable,
    INamed {
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

    open var defaultValue: String = ""

    companion object {
        fun emptyOfType(type: Type, index: Int, groupId: Long): TrackedThing =
            when (type) {
                Type.None -> throw Exception("Cannot create tracked thing of type None.")
                Type.Percentage -> PercentageTrackedThing(0, "", 0f, index, groupId)
                Type.Health -> HealthTrackedThing(0, 0, "", 0, index, groupId)
                Type.Ability -> AbilityTrackedThing(0, "", 0, index, groupId)
                Type.SpellSlot -> SpellSlotTrackedThing(1, 0, "", 0, index, groupId)
                Type.Number -> NumberTrackedThing(0, "", 0, index, groupId)
                    .apply { defaultValue = Int.MAX_VALUE.toString() }

                Type.SpellList -> SpellListTrackedThing(0, "", "[]", index, groupId)
                Type.Text -> TextTrackedThing(0, "", "", index, groupId)
                Type.HitDice -> HitDiceTrackedThing(0, "", 0, index, groupId)
                Type.FiveEStats -> StatsTrackedThing(0, "", "[]", index, groupId)
            }

        @Keep
        object Empty : TrackedThing(name = "", type = Type.None) {
            override fun setNewValue(value: String) {}
            override fun getPrintableValue(): String = ""
            override fun add(delta: String) {}
            override fun subtract(delta: String) {}
            override fun copy(): TrackedThing = this
            override fun canAdd(): Boolean = false
            override fun canSubtract(): Boolean = false
        }
    }

    abstract fun setNewValue(value: String)

    open fun isValueValid(): Boolean = value.isNotBlank()

    open fun validate(): Boolean = isValueValid()

    open fun resetValueToDefault() = setNewValue(defaultValue)

    abstract fun getPrintableValue(): String
    abstract fun add(delta: String)
    abstract fun subtract(delta: String)
    abstract fun copy(): TrackedThing
    abstract fun canAdd(): Boolean
    abstract fun canSubtract(): Boolean
}