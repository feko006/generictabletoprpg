package com.feko.generictabletoprpg.shared.features.io.domain.model

import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup
import kotlinx.serialization.Serializable

@DoNotObfuscate
@Serializable
data class AppModel(
    val sources: List<Source> = mutableListOf(),
    val trackedGroups: List<TrackedThingGroup> = mutableListOf()
)