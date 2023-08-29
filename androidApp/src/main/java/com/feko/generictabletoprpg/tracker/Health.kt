package com.feko.generictabletoprpg.tracker

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