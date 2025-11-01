package com.feko.generictabletoprpg.shared.features.weapon

import com.feko.generictabletoprpg.shared.common.domain.IProcessEdnMap
import com.feko.generictabletoprpg.shared.common.domain.model.Damage
import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.common.domain.model.IFromSource
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import kotlinx.serialization.Serializable


@DoNotObfuscate
@Serializable
data class Weapon(
    override val id: Long = 0,
    override val name: String,
    override val source: String,
    val damage: Damage?,
    val type: String,
    val isRanged: Boolean,
    var isMelee: Boolean,
    val range: RangedWeaponRange?,
    val isTwoHanded: Boolean,
    val requiresAmmunition: Boolean,
    val isThrown: Boolean,
    val isFinesse: Boolean,
    val isLight: Boolean,
    val isHeavy: Boolean,
    val isReach: Boolean,
    val isSpecial: Boolean,
    val subType: String
) : IIdentifiable,
    INamed,
    IFromSource {

    init {
        if (!isMelee && !isRanged) {
            // By default melee
            isMelee = true
        }
    }

    val readableRange: String
        get() =
            if (isMelee) {
                "Melee"
            } else if (isRanged) {
                checkNotNull(range)
                "Ranged (${range.ft()})"
            } else {
                throw IllegalStateException("Unknown range of weapon")
            }

    val hasAnyProperties: Boolean
        get() = isTwoHanded or
                requiresAmmunition or
                isThrown or
                isFinesse or
                isLight or
                isHeavy or
                isReach or
                isSpecial

    val properties: String
        get() {
            val builder = StringBuilder()
            if (isTwoHanded) {
                builder.append("Two-handed")
            }
            if (requiresAmmunition) {
                if (builder.isNotBlank()) {
                    builder.append(", ")
                }
                builder.append("Ammunition")
            }
            if (isThrown) {
                if (builder.isNotBlank()) {
                    builder.append(", ")
                }
                builder.append("Thrown")
            }
            if (isFinesse) {
                if (builder.isNotBlank()) {
                    builder.append(", ")
                }
                builder.append("Finesse")
            }
            if (isLight) {
                if (builder.isNotBlank()) {
                    builder.append(", ")
                }
                builder.append("Light")
            }
            if (isHeavy) {
                if (builder.isNotBlank()) {
                    builder.append(", ")
                }
                builder.append("Heavy")
            }
            if (isReach) {
                if (builder.isNotBlank()) {
                    builder.append(", ")
                }
                builder.append("Reach")
            }
            if (isSpecial) {
                if (builder.isNotBlank()) {
                    builder.append(", ")
                }
                builder.append("Special")
            }
            return builder.toString()
        }

    // In feat
    @DoNotObfuscate
    @Serializable
    data class RangedWeaponRange(
        val minimum: Int,
        val maximum: Int
    ) {
        fun ft(): String =
            "$minimum/$maximum ft"

        companion object {
            fun createFromOrcbrewData(
                processEdnMapPort: IProcessEdnMap,
                rangeMap: Map<Any, Any>
            ): RangedWeaponRange =
                RangedWeaponRange(
                    processEdnMapPort.getValue(rangeMap, ":min"),
                    processEdnMapPort.getValue(rangeMap, ":max")
                )
        }
    }

    companion object {
        fun createFromOrcbrewData(
            processEdnMapPort: IProcessEdnMap,
            weaponMap: Map<Any, Any>,
            defaultSource: String
        ): Weapon {
            var damage: Damage? = null
            if (processEdnMapPort.containsKey(weaponMap, ":damage-type")
                && processEdnMapPort.containsKey(weaponMap, ":damage-die")
                && processEdnMapPort.containsKey(weaponMap, ":damage-die-count")
            ) {
                val damageType =
                    processEdnMapPort.getValue<Any>(weaponMap, ":damage-type")
                        .toString()
                        .substring(1)
                val damageDie =
                    processEdnMapPort.getValue<Int>(weaponMap, ":damage-die")
                val damageDieCount =
                    processEdnMapPort.getValue<Int>(weaponMap, ":damage-die-count")
                damage = Damage(damageType, damageDie, damageDieCount)
            }
            val type = processEdnMapPort.getValue<Any>(weaponMap, ":type")
                .toString()
                .substring(1)
            var range: RangedWeaponRange? = null
            if (processEdnMapPort.containsKey(weaponMap, ":range")) {
                range = RangedWeaponRange.createFromOrcbrewData(
                    processEdnMapPort,
                    processEdnMapPort.getValue(weaponMap, ":range")
                )
            }
            return Weapon(
                0,
                processEdnMapPort.getValue(weaponMap, ":name"),
                defaultSource,
                damage,
                type,
                processEdnMapPort.getValueOrDefault(weaponMap, ":ranged?", false),
                processEdnMapPort.getValueOrDefault(weaponMap, ":melee?", false),
                range,
                processEdnMapPort.getValueOrDefault(weaponMap, ":two-handed?", false),
                processEdnMapPort.getValueOrDefault(weaponMap, ":ammunition?", false),
                processEdnMapPort.getValueOrDefault(weaponMap, ":thrown?", false),
                processEdnMapPort.getValueOrDefault(weaponMap, ":finesse?", false),
                processEdnMapPort.getValueOrDefault(weaponMap, ":light?", false),
                processEdnMapPort.getValueOrDefault(weaponMap, ":heavy?", false),
                processEdnMapPort.getValueOrDefault(weaponMap, ":reach?", false),
                processEdnMapPort.getValueOrDefault(weaponMap, ":special?", false),
                processEdnMapPort.getValueOrDefault(weaponMap, ":sub-type", "")
            )
        }
    }
}