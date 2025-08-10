package com.feko.generictabletoprpg.features.io.domain.model

import androidx.annotation.Keep
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThingGroup
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class AppModel(
    val sources: List<Source> = mutableListOf(),
    val trackedGroups: List<TrackedThingGroup> = mutableListOf()
)