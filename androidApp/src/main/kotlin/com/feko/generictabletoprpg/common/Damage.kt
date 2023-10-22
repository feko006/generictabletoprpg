package com.feko.generictabletoprpg.common

@DoNotObfuscate
data class Damage(
    val damageType: String,
    val damageDie: Int,
    val damageDieCount: Int
) {
    override fun toString(): String =
        "${damageDieCount}d${damageDie} $damageType"
}