package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
class HitDice(
    id: Long = 0L,
    name: String,
    amount: Int,
    index: Int = 0,
    groupId: Long = 0L
) : IntTrackedThing(id, name, amount, Type.HitDice, index, groupId) {
    override fun copy(): TrackedThing =
        HitDice(id, name, amount, index, groupId).also { it.defaultValue = defaultValue }

    override fun resetValueToDefault() {
        val maximumValue = defaultValue.toInt()
        val toAdd = (maximumValue / 2f).toInt()
        val newValue = (amount + toAdd).coerceAtMost(maximumValue)
        setNewValue(newValue.toString())
    }
}