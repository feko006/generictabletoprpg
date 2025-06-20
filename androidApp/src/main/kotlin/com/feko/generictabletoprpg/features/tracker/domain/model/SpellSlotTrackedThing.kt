package com.feko.generictabletoprpg.features.tracker.domain.model

import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate

@DoNotObfuscate
class SpellSlotTrackedThing(
    var level: Int,
    id: Long = 0L,
    name: String,
    amount: Int,
    index: Int = 0,
    groupId: Long = 0L
) : IntTrackedThing(id, name, amount, Type.SpellSlot, index, groupId) {

    fun isLevelValid(): Boolean = level > 0

    override fun validate(): Boolean = super.validate() && isLevelValid()

    override fun isValueValid(): Boolean = super.isValueValid() && amount > 0

    override fun copy(): TrackedThing =
        SpellSlotTrackedThing(level, id, name, amount, index, groupId).also { it.defaultValue = defaultValue }
}