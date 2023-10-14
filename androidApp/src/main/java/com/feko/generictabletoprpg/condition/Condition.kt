package com.feko.generictabletoprpg.condition

import com.feko.generictabletoprpg.common.IFromSource
import com.feko.generictabletoprpg.common.IIdentifiable
import com.feko.generictabletoprpg.common.INamed

data class Condition(
    override val id: Long = 0,
    override val name: String,
    val description: String,
    override var source: String = ""
) : IIdentifiable,
    INamed,
    IFromSource