package com.feko.generictabletoprpg.tracker

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