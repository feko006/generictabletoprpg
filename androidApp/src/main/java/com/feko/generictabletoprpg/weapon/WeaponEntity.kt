package com.feko.generictabletoprpg.weapon

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.*

@Entity(
    tableName = "weapons"
)
data class WeaponEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    override val source: String,
    @Embedded
    val damage: DamageEmbeddedEntity?,
    val type: String,
    val isRanged: Boolean,
    val isMelee: Boolean,
    @Embedded
    val range: RangedWeaponRangeEmbeddedEntity?,
    val isTwoHanded: Boolean,
    val requiresAmmunition: Boolean,
    val isThrown: Boolean,
    val isFinesse: Boolean,
    val isLight: Boolean,
    val isHeavy: Boolean,
    val isReach: Boolean,
    val isSpecial: Boolean,
    val subType: String
) : Named,
    MutableIdentifiable,
    FromSource,
    CoreConvertible<Weapon> {

    override fun toCoreModel(): Weapon =
        Weapon(
            id,
            name,
            source,
            damage?.toCoreModel(),
            type,
            isRanged,
            isMelee,
            range?.toCoreModel(),
            isTwoHanded,
            requiresAmmunition,
            isThrown,
            isFinesse,
            isLight,
            isHeavy,
            isReach,
            isSpecial,
            subType
        )

    companion object {
        fun fromCoreModel(weapon: Weapon): WeaponEntity {
            val damage: DamageEmbeddedEntity? =
                weapon.damage?.let {
                    DamageEmbeddedEntity.fromCoreModel(it)
                }
            val range: RangedWeaponRangeEmbeddedEntity? =
                weapon.range?.let {
                    RangedWeaponRangeEmbeddedEntity.fromCoreModel(it)
                }
            return WeaponEntity(
                weapon.id,
                weapon.name,
                weapon.source,
                damage,
                weapon.type,
                weapon.isRanged,
                weapon.isMelee,
                range,
                weapon.isTwoHanded,
                weapon.requiresAmmunition,
                weapon.isThrown,
                weapon.isFinesse,
                weapon.isLight,
                weapon.isHeavy,
                weapon.isReach,
                weapon.isSpecial,
                weapon.subType
            )
        }
    }

    data class RangedWeaponRangeEmbeddedEntity(
        val minimum: Int,
        val maximum: Int
    ) : CoreConvertible<Weapon.RangedWeaponRange> {
        override fun toCoreModel(): Weapon.RangedWeaponRange =
            Weapon.RangedWeaponRange(minimum, maximum)

        companion object {
            fun fromCoreModel(range: Weapon.RangedWeaponRange) =
                RangedWeaponRangeEmbeddedEntity(
                    range.minimum,
                    range.maximum
                )
        }
    }
}
