package com.feko.generictabletoprpg.shared.common.domain.model

import kotlinx.serialization.Serializable

@DoNotObfuscate
@Serializable
data class Damage(
    val damageType: String,
    val damageDie: Int,
    val damageDieCount: Int
) {
    override fun toString(): String =
        "${damageDieCount}d${damageDie} $damageType"
}