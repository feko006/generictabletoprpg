package com.feko.generictabletoprpg.shared.features.tracker.model

import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingGroupEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@DoNotObfuscate
@Serializable
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