package com.feko.generictabletoprpg.tracker

class Ability(id: Long, name: String, amount: Int, groupId: Long) :
    IntTrackedThing(id, name, amount, Type.Ability, groupId) {

    override fun copy(): TrackedThing =
        Ability(id, name, amount, groupId).also { it.defaultValue = defaultValue }
}

