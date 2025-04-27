package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.IIdentifiable
import com.feko.generictabletoprpg.common.INamed

@DoNotObfuscate
data class TrackedThingGroup(
    @Transient
    override var id: Long = 0L,
    override val name: String,
    var trackedThings: List<TrackedThing> = mutableListOf()
) : IIdentifiable,
    INamed {

    fun toEntity(): TrackedThingGroupEntity = TrackedThingGroupEntity(id, name)

    companion object {
        val Empty = TrackedThingGroup(0L, "")
    }
}