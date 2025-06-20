package com.feko.generictabletoprpg.features.condition

import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.common.domain.model.IFromSource
import com.feko.generictabletoprpg.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed

@DoNotObfuscate
data class Condition(
    override val id: Long = 0,
    override val name: String,
    val description: String,
    override var source: String = ""
) : IIdentifiable,
    INamed,
    IFromSource