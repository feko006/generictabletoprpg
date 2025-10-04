package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.features.tracker.TrackedThingGroupEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Keep
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