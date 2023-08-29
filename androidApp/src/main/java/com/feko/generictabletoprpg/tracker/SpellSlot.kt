package com.feko.generictabletoprpg.tracker

class SpellSlot(var level: Int, id: Long, name: String, amount: Int, groupId: Long) :
    IntTrackedThing(id, name, amount, Type.SpellSlot, groupId) {

    fun isLevelValid(): Boolean = level > 0

    override fun validate(): Boolean = super.validate() && isLevelValid()

    override fun copy(): TrackedThing =
        SpellSlot(level, id, name, amount, groupId).also { it.defaultValue = defaultValue }
}