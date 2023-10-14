package com.feko.generictabletoprpg.common

data class DamageEmbeddedEntity(
    val damageType: String,
    val damageDie: Int,
    val damageDieCount: Int
) : ICoreConvertible<Damage> {
    override fun toCoreModel(): Damage =
        Damage(damageType, damageDie, damageDieCount)

    companion object {
        fun fromCoreModel(damage: Damage) =
            DamageEmbeddedEntity(
                damage.damageType,
                damage.damageDie,
                damage.damageDieCount
            )
    }
}
