package com.feko.generictabletoprpg.features.io.domain.model

import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThingGroup

@DoNotObfuscate
data class AppModel(
    val sources: List<Source> = mutableListOf(),
    val trackedGroups: List<TrackedThingGroup> = mutableListOf()
)