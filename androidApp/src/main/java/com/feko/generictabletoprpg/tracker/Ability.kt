package com.feko.generictabletoprpg.tracker

class Ability(
    id: Long = 0L,
    name: String,
    amount: Int,
    index: Int = 0,
    groupId: Long = 0L
) : IntTrackedThing(id, name, amount, Type.Ability, index, groupId) {

    override fun copy(): TrackedThing =
        Ability(id, name, amount, index, groupId).also { it.defaultValue = defaultValue }
}

