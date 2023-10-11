package com.feko.generictabletoprpg.app

import com.feko.generictabletoprpg.tracker.TrackedThingGroup

data class AppModel(
    val sources: List<Source> = mutableListOf(),
    val trackedGroups: List<TrackedThingGroup> = mutableListOf()
)