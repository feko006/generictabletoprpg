package com.feko.generictabletoprpg.features.tracker.domain.model

import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate

@DoNotObfuscate
class AbilityTrackedThing(
    id: Long = 0L,
    name: String,
    amount: Int,
    index: Int = 0,
    groupId: Long = 0L
) : IntTrackedThing(id, name, amount, Type.Ability, index, groupId) {

    override fun copy(): TrackedThing =
        AbilityTrackedThing(id, name, amount, index, groupId).also { it.defaultValue = defaultValue }
}

