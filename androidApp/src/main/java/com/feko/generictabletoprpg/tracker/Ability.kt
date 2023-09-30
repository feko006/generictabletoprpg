package com.feko.generictabletoprpg.tracker

class Ability(id: Long, name: String, amount: Int, index: Int, groupId: Long) :
    IntTrackedThing(id, name, amount, Type.Ability, index, groupId) {

    override fun copy(): TrackedThing =
        Ability(id, name, amount, index, groupId).also { it.defaultValue = defaultValue }
}

