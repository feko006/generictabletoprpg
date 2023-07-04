package com.feko.generictabletoprpg.tracker

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
        fun emptyOfType(type: Type, groupId: Long): TrackedThing =
            when (type) {
                Type.None -> throw Exception("Cannot create tracked thing of type None.")
                Type.Percentage -> Percentage(0, "", 0f, groupId)
                Type.Health -> Health(0, 0, "", 0, groupId)
                Type.Ability -> Ability(0, "", 0, groupId)
                Type.SpellSlot -> SpellSlot(1, 0, "", 0, groupId)
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

    class Percentage(id: Long, name: String, var amount: Float, groupId: Long) :
        TrackedThing(id, name, "", Type.Percentage, groupId) {

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
            Percentage(id, name, amount, groupId).also { it.defaultValue = defaultValue }

        override fun canAdd(): Boolean = amount < 100f

        override fun canSubtract(): Boolean = amount > 0f
    }

    abstract class GenericTrackedThing<T>(
        id: Long,
        name: String,
        var amount: T,
        type: Type,
        groupId: Long
    ) :
        TrackedThing(id, name, amount.toString(), type, groupId)

    abstract class IntTrackedThing(
        id: Long,
        name: String,
        amount: Int,
        type: Type,
        groupId: Long
    ) :
        GenericTrackedThing<Int>(id, name, amount, type, groupId) {

        init {
            value = toValue(amount)
        }

        companion object {
            @JvmStatic
            protected fun toValue(amount: Int) = amount.toString()

            @JvmStatic
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

    class Health(var temporaryHp: Int, id: Long, name: String, amount: Int, groupId: Long) :
        IntTrackedThing(id, name, amount, Type.Health, groupId) {

        override fun copy(): TrackedThing =
            Health(temporaryHp, id, name, amount, groupId).also { it.defaultValue = defaultValue }

        override fun subtract(delta: String) {
            var intDelta = toAmount(delta)
            val newTemporaryHp = (temporaryHp - intDelta).coerceAtLeast(0)
            intDelta -= (temporaryHp - newTemporaryHp)
            temporaryHp = newTemporaryHp
            super.subtract(toValue(intDelta))
        }

        fun addTemporaryHp(value: String) {
            val newTemporaryHp = toAmount(value)
            if (temporaryHp < newTemporaryHp) {
                temporaryHp = newTemporaryHp
            }
        }
    }

    class Ability(id: Long, name: String, amount: Int, groupId: Long) :
        IntTrackedThing(id, name, amount, Type.Ability, groupId) {

        override fun copy(): TrackedThing =
            Ability(id, name, amount, groupId).also { it.defaultValue = defaultValue }
    }

    class SpellSlot(var level: Int, id: Long, name: String, amount: Int, groupId: Long) :
        IntTrackedThing(id, name, amount, Type.SpellSlot, groupId) {

        fun isLevelValid(): Boolean = level > 0

        override fun validate(): Boolean = super.validate() && isLevelValid()

        override fun copy(): TrackedThing =
            SpellSlot(level, id, name, amount, groupId).also { it.defaultValue = defaultValue }
    }
}