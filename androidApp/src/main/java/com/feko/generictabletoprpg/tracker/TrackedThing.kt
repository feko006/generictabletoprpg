package com.feko.generictabletoprpg.tracker

import androidx.annotation.StringRes
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.MutableIdentifiable
import com.feko.generictabletoprpg.common.Named

sealed class TrackedThing(
    override var id: Long,
    override var name: String,
    var value: String = "",
    val type: Type,
    var index: Int = 0,
    var groupId: Long = 0L
) : MutableIdentifiable,
    Named {
    enum class Type(@StringRes val nameResource: Int) {
        None(0),
        Percentage(R.string.percentage),
        Health(R.string.health),
        Ability(R.string.ability),
        SpellSlot(R.string.spell_slot),
        Number(R.string.number)
    }

    var defaultValue: String = ""

    companion object {
        fun emptyOfType(type: Type, index: Int, groupId: Long): TrackedThing =
            when (type) {
                Type.None -> throw Exception("Cannot create tracked thing of type None.")
                Type.Percentage -> Percentage(0, "", 0f, index, groupId)
                Type.Health -> Health(0, 0, "", 0, index, groupId)
                Type.Ability -> Ability(0, "", 0, index, groupId)
                Type.SpellSlot -> SpellSlot(1, 0, "", 0, index, groupId)
                Type.Number -> Number(0, "", 0, index, groupId)
                    .apply { defaultValue = Int.MAX_VALUE.toString() }
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