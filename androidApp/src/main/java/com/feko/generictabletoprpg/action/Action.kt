package com.feko.generictabletoprpg.action

import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.IFromSource
import com.feko.generictabletoprpg.common.IIdentifiable
import com.feko.generictabletoprpg.common.INamed

@DoNotObfuscate
data class Action(
    override val id: Long = 0,
    override val name: String,
    val description: String,
    override var source: String = ""
) : IIdentifiable,
    INamed,
    IFromSource