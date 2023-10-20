package com.feko.generictabletoprpg.app

import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.tracker.TrackedThingGroup

@DoNotObfuscate
data class AppModel(
    val sources: List<Source> = mutableListOf(),
    val trackedGroups: List<TrackedThingGroup> = mutableListOf()
)