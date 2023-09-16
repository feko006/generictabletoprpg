package com.feko.generictabletoprpg.tracker

import androidx.annotation.StringRes
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.MutableIdentifiable
import com.feko.generictabletoprpg.common.Named

sealed class TrackedThing(
    override var id: Long,
    override var name: String,
    value: String,
    val type: Type,
    val groupId: Long
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

    var value: String = value
        protected set
    var defaultValue: String = ""

    companion object {
        fun emptyOfType(type: Type, groupId: Long): TrackedThing =
            when (type) {
                Type.None -> throw Exception("Cannot create tracked thing of type None.")
                Type.Percentage -> Percentage(0, "", 0f, groupId)
                Type.Health -> Health(0, 0, "", 0, groupId)
                Type.Ability -> Ability(0, "", 0, groupId)
                Type.SpellSlot -> SpellSlot(1, 0, "", 0, groupId)
                Type.Number -> Number(0, "", 0, groupId)
                    .apply { defaultValue = Int.MAX_VALUE.toString() }
            }
    }

    abstract fun setNewValue(value: String)

    fun isNameValid(): Boolean = name.isNotBlank()

    open fun isValueValid(): Boolean = value.isNotBlank()

    open fun validate(): Boolean = isNameValid() && isValueValid()

    open fun resetValueToDefault() = setNewValue(defaultValue)

    abstract fun getPrintableValue(): String
    abstract fun add(delta: String)
    abstract fun subtract(delta: String)
    abstract fun copy(): TrackedThing
    abstract fun canAdd(): Boolean
    abstract fun canSubtract(): Boolean
}