package com.feko.generictabletoprpg.common.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Damage(
    val damageType: String,
    val damageDie: Int,
    val damageDieCount: Int
) {
    override fun toString(): String =
        "${damageDieCount}d${damageDie} $damageType"
}