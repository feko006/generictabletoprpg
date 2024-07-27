package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
class Number(
    id: Long = 0L,
    name: String,
    amount: Int,
    index: Int = 0,
    groupId: Long = 0L
) : IntTrackedThing(id, name, amount, Type.Number, index, groupId) {

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