package com.feko.generictabletoprpg.shared.common.data.local.entities

import com.feko.generictabletoprpg.shared.common.domain.model.Damage
import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.common.domain.model.ICoreConvertible

@DoNotObfuscate
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
