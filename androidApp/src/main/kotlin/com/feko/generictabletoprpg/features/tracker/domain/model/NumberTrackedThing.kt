package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep

@Keep
class NumberTrackedThing(
    id: Long = 0L,
    name: String,
    amount: Int,
    index: Int = 0,
    groupId: Long = 0L
) : IntTrackedThing(id, name, amount, Type.Number, index, groupId) {

    override fun copy(): TrackedThing =
        NumberTrackedThing(id, name, amount, index, groupId).also { it.defaultValue = defaultValue }

    override fun isValueValid(): Boolean = true

    override fun getPrintableValue(): String = value

    override fun canAdd(): Boolean = true

    override fun canSubtract(): Boolean = true

    override fun add(delta: String) = addInternal(delta, coerceToDefaultValue = false)

    override fun subtract(delta: String) = subtractInternal(delta, coerceToZero = false)

    override fun resetValueToDefault() = Unit

    companion object {
        val Empty = NumberTrackedThing(0L, "", 0, 0, 0L)
    }
}