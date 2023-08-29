package com.feko.generictabletoprpg.common

data class Damage(
    val damageType: String,
    val damageDie: Int,
    val damageDieCount: Int
) {
    override fun toString(): String =
        "${damageDieCount}d${damageDie} $damageType"
}