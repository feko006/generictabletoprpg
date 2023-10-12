package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.Named

data class TrackedThingGroup(
    @Transient
    override var id: Long = 0L,
    override val name: String,
    var trackedThings: List<TrackedThing> = mutableListOf()
) : Identifiable,
    Named