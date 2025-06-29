package com.feko.generictabletoprpg.features.disease

import androidx.annotation.Keep
import com.feko.generictabletoprpg.common.domain.model.IFromSource
import com.feko.generictabletoprpg.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed

@Keep
data class Disease(
    override val id: Long = 0,
    override val name: String,
    val description: String,
    override var source: String = ""
) : IIdentifiable,
    INamed,
    IFromSource