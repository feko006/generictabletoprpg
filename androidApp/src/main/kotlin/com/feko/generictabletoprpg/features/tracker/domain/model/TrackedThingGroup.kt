package com.feko.generictabletoprpg.features.tracker.domain.model

import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed
import com.feko.generictabletoprpg.features.tracker.TrackedThingGroupEntity

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