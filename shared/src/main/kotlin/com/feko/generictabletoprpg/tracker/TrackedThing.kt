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

    fun resetValueToDefault() = setNewValue(defaultValue)

    abstract fun getPrintableValue(): String
    abstract fun add(delta: String)
    abstract fun subtract(delta: String)
    abstract fun copy(): TrackedThing
    abstract fun canAdd(): Boolean
    abstract fun canSubtract(): Boolean

    class Percentage(id: Long, name: String, var amount: Float) :
        TrackedThing(id, name, "", Type.Percentage) {

        init {
            value = toValue(amount)
        }

        companion object {
            private fun toValue(amount: Float) = String.format("%.2f", amount)
            private fun toAmount(value: String) = (value.toFloatOrNull() ?: 0f).coerceIn(0f, 100f)
        }

        override fun setNewValue(value: String) {
            this.value = value
            amount = value.toFloatOrNull() ?: -1f
        }

        override fun isValueValid(): Boolean =
            super.isValueValid() && amount >= 0f && amount <= 100f

        override fun getPrintableValue(): String = "${value}%"
        override fun add(delta: String) {
            val floatDelta = toAmount(delta)
            amount = (amount + floatDelta).coerceAtMost(100f)
            value = toValue(amount)
        }

        override fun subtract(delta: String) {
            val floatDelta = toAmount(delta)
            amount = (amount - floatDelta).coerceAtLeast(0f)
            value = toValue(amount)
        }

        override fun copy(): TrackedThing =
            Percentage(id, name, amount).also { it.defaultValue = defaultValue }

        override fun canAdd(): Boolean = amount < 100f

        override fun canSubtract(): Boolean = amount > 0f
    }

    abstract class GenericTrackedThing<T>(id: Long, name: String, var amount: T, type: Type) :
        TrackedThing(id, name, amount.toString(), type)

    abstract class IntTrackedThing(id: Long, name: String, amount: Int, type: Type) :
        GenericTrackedThing<Int>(id, name, amount, type) {

        init {
            value = toValue(amount)
        }

        companion object {
            protected fun toValue(amount: Int) = amount.toString()
            protected fun toAmount(value: String) = value.toIntOrNull() ?: 0
        }

        override fun setNewValue(value: String) {
            this.value = value
            amount = value.toIntOrNull() ?: -1
        }

        override fun isValueValid(): Boolean =
            super.isValueValid() && amount >= 0 && amount <= toAmount(defaultValue)

        override fun getPrintableValue(): String = "$value / $defaultValue"

        override fun add(delta: String) {
            val intDelta = toAmount(delta)
            amount = (amount + intDelta).coerceAtMost(toAmount(defaultValue))
            value = toValue(amount)
        }

        override fun subtract(delta: String) {
            val intDelta = toAmount(delta)
            amount = (amount - intDelta).coerceAtLeast(0)
            value = toValue(amount)
        }

        override fun canAdd(): Boolean = amount < toAmount(defaultValue)

        override fun canSubtract(): Boolean = amount > 0
    }

    class Health(id: Long, name: String, amount: Int) :
        IntTrackedThing(id, name, amount, Type.Health) {

        override fun copy(): TrackedThing =
            Health(id, name, amount).also { it.defaultValue = defaultValue }
    }

    class Ability(id: Long, name: String, amount: Int) :
        IntTrackedThing(id, name, amount, Type.Ability) {

        override fun copy(): TrackedThing =
            Ability(id, name, amount).also { it.defaultValue = defaultValue }
    }

    class SpellSlot(var level: Int, id: Long, name: String, amount: Int) :
        IntTrackedThing(id, name, amount, Type.SpellSlot) {

        fun isLevelValid(): Boolean = level > 0

        override fun validate(): Boolean = super.validate() && isLevelValid()

        override fun copy(): TrackedThing =
            SpellSlot(level, id, name, amount).also { it.defaultValue = defaultValue }
    }
}