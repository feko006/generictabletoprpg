package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.Named

sealed class TrackedThing(
    override var id: Long,
    override var name: String,
    value: String,
    val type: Type
) : Identifiable,
    Named {
    enum class Type {
        None,
        Percentage,
        Health,
        Ability,
        SpellSlot
    }

    var value: String = value
        protected set
    var defaultValue: String = ""

    companion object {
        fun emptyOfType(type: Type): TrackedThing =
            when (type) {
                Type.None -> throw Exception("Cannot create tracked thing of type None.")
                Type.Percentage -> Percentage(0, "", 0f)
                Type.Health -> Health(0, "", 0)
                Type.Ability -> Ability(0, "", 0)
                Type.SpellSlot -> SpellSlot(1, 0, "", 0)
            }
    }

    abstract fun setNewValue(value: String)

    fun isNameValid(): Boolean = name.isNotBlank()

    open fun isValueValid(): Boolean = value.isNotBlank()

    open fun validate(): Boolean = isNameValid() && isValueValid()

    abstract fun getPrintableValue(): String

    class Percentage(id: Long, name: String, var amount: Float) :
        TrackedThing(id, name, String.format("%.2f", amount), Type.Percentage) {
        override fun setNewValue(value: String) {
            this.value = value
            amount = value.toFloatOrNull() ?: -1f
        }

        override fun isValueValid(): Boolean =
            super.isValueValid() && amount >= 0f && amount <= 100f

        override fun getPrintableValue(): String = "${value}%"
    }

    abstract class GenericTrackedThing<T>(id: Long, name: String, var amount: T, type: Type) :
        TrackedThing(id, name, amount.toString(), type)

    abstract class IntTrackedThing(id: Long, name: String, amount: Int, type: Type) :
        GenericTrackedThing<Int>(id, name, amount, type) {
        override fun setNewValue(value: String) {
            this.value = value
            amount = value.toIntOrNull() ?: -1
        }

        override fun isValueValid(): Boolean = super.isValueValid() && amount > 0

        override fun getPrintableValue(): String = "$value / $defaultValue"
    }

    class Health(id: Long, name: String, amount: Int) :
        IntTrackedThing(id, name, amount, Type.Health)

    class Ability(id: Long, name: String, amount: Int) :
        IntTrackedThing(id, name, amount, Type.Ability)

    class SpellSlot(var level: Int, id: Long, name: String, amount: Int) :
        IntTrackedThing(id, name, amount, Type.SpellSlot) {

        fun isLevelValid(): Boolean = level > 0

        override fun validate(): Boolean = super.validate() && isLevelValid()
    }
}