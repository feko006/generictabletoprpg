package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common

import com.feko.generictabletoprpg.common.CoreConvertible
import com.feko.generictabletoprpg.common.Damage

data class DamageEmbeddedEntity(
    val damageType: String,
    val damageDie: Int,
    val damageDieCount: Int
) : CoreConvertible<Damage> {
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
