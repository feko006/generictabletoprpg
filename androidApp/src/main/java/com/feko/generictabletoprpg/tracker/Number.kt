package com.feko.generictabletoprpg.tracker

class Number(id: Long, name: String, amount: Int, index: Int, groupId: Long) :
    IntTrackedThing(id, name, amount, Type.Number, index, groupId) {

    override fun copy(): TrackedThing =
        Number(id, name, amount, index, groupId).also { it.defaultValue = defaultValue }

    override fun isValueValid(): Boolean = true

    override fun getPrintableValue(): String = value

    override fun canAdd(): Boolean = true

    override fun canSubtract(): Boolean = true

    override fun add(delta: String) = addInternal(delta, coerceToDefaultValue = false)

    override fun subtract(delta: String) = subtractInternal(delta, coerceToZero = false)

    override fun resetValueToDefault() = Unit
}