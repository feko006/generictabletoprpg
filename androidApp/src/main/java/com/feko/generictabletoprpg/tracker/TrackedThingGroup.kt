package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.Named

data class TrackedThingGroup(
    override var id: Long,
    override val name: String
) : Identifiable,
    Named