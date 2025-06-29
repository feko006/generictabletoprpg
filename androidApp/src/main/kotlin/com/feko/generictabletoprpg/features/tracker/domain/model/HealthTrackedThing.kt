package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep

@Keep
class HealthTrackedThing(
    var temporaryHp: Int,
    id: Long = 0L,
    name: String,
    amount: Int,
    index: Int = 0,
    groupId: Long = 0L
) : IntTrackedThing(id, name, amount, Type.Health, index, groupId) {

    override fun copy(): TrackedThing =
        HealthTrackedThing(temporaryHp, id, name, amount, index, groupId).also {
            it.defaultValue = defaultValue
        }

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

    companion object {
        val Empty = HealthTrackedThing(0, 0L, "", 0, 0, 0L)
    }
}