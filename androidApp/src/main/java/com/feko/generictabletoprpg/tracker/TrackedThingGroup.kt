package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.Named

data class TrackedThingGroup(
    override var id: Long = 0L,
    override val name: String,
    val trackedThings: List<TrackedThing> = mutableListOf()
) : Identifiable,
    Named