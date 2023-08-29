package com.feko.generictabletoprpg.action

import com.feko.generictabletoprpg.common.FromSource
import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.Named

data class Action(
    override val id: Long = 0,
    override val name: String,
    val description: String,
    override var source: String = ""
) : Identifiable,
    Named,
    FromSource