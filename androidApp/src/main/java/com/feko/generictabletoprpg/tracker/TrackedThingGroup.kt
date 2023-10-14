package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.IIdentifiable
import com.feko.generictabletoprpg.common.INamed

data class TrackedThingGroup(
    @Transient
    override var id: Long = 0L,
    override val name: String,
    var trackedThings: List<TrackedThing> = mutableListOf()
) : IIdentifiable,
    INamed