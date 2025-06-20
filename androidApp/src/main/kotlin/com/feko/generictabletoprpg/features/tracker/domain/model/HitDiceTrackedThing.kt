package com.feko.generictabletoprpg.features.tracker.domain.model

import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate

@DoNotObfuscate
class HitDiceTrackedThing(
    id: Long = 0L,
    name: String,
    amount: Int,
    index: Int = 0,
    groupId: Long = 0L
) : IntTrackedThing(id, name, amount, Type.HitDice, index, groupId) {
    override fun copy(): TrackedThing =
        HitDiceTrackedThing(id, name, amount, index, groupId).also { it.defaultValue = defaultValue }

    override fun resetValueToDefault() {
        val maximumValue = defaultValue.toInt()
        val toAdd = (maximumValue / 2f).toInt().coerceAtLeast(1)
        val newValue = (amount + toAdd).coerceAtMost(maximumValue)
        setNewValue(newValue.toString())
    }
}