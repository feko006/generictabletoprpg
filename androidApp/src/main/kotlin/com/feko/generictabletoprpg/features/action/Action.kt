package com.feko.generictabletoprpg.features.action

import androidx.annotation.Keep
import com.feko.generictabletoprpg.shared.common.domain.model.IFromSource
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Action(
    override val id: Long = 0,
    override val name: String,
    val description: String,
    override var source: String = ""
) : IIdentifiable,
    INamed,
    IFromSource