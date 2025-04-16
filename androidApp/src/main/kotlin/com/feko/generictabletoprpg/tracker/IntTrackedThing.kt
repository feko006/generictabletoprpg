package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
abstract class IntTrackedThing(
    id: Long,
    name: String,
    amount: Int,
    type: Type,
    index: Int,
    groupId: Long
) : GenericTrackedThing<Int>(id, name, amount, type, index, groupId) {

    init {
        value = toValue(amount)
    }

    private var _defaultValue = "0"
    override var defaultValue: String
        get() = _defaultValue
        set(value) {
            _defaultValue = normalize(value)
        }

    companion object {
        @JvmStatic
        protected fun toValue(amount: Int) = amount.toString()

        @JvmStatic
        protected fun toAmount(value: String) = value.toIntOrNull() ?: 0
    }

    override fun setNewValue(value: String) {
        this.value = normalize(value)
        amount = value.toIntOrNull() ?: -1
    }

    private fun normalize(value: String) = value.toIntOrNull()?.toString() ?: "0"

    override fun isValueValid(): Boolean =
        super.isValueValid() && amount >= 0 && amount <= toAmount(defaultValue)

    override fun getPrintableValue(): String = "$value / $defaultValue"

    override fun add(delta: String) {
        addInternal(delta, coerceToDefaultValue = true)
    }

    protected fun addInternal(delta: String, coerceToDefaultValue: Boolean) {
        val intDelta = toAmount(delta)
        amount = (amount + intDelta)
        if (coerceToDefaultValue) {
            amount = amount.coerceAtMost(toAmount(defaultValue))
        }
        value = toValue(amount)
    }

    override fun subtract(delta: String) {
        subtractInternal(delta, coerceToZero = true)
    }

    protected fun subtractInternal(delta: String, coerceToZero: Boolean) {
        val intDelta = toAmount(delta)
        amount = (amount - intDelta)
        if (coerceToZero) {
            amount = amount.coerceAtLeast(0)
        }
        value = toValue(amount)
    }

    override fun canAdd(): Boolean = amount < toAmount(defaultValue)

    override fun canSubtract(): Boolean = amount > 0
}