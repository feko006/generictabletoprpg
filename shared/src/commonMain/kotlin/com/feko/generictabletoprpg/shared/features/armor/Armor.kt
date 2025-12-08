package com.feko.generictabletoprpg.shared.features.armor

import com.feko.generictabletoprpg.shared.common.domain.IProcessEdnMap
import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.common.domain.model.IFromSource
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.IKClassProvider
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@DoNotObfuscate
@Serializable
data class Armor(
    override val id: Long = 0,
    override val name: String,
    override val source: String,
    val type: String,
    val baseAc: Int?,
    val maxDexModifier: Int?,
    val stealthDisadvantage: Boolean?,
    // In lb
    val weight: Int?,
    val minimumStrength: Int?
) : IIdentifiable,
    INamed,
    IFromSource,
    IKClassProvider {
    val weightInLbs
        get() = "$weight lbs"

    override val kclass: KClass<*>
        get() = Armor::class

    companion object {
        fun createFromOrcbrewData(
            processEdnMapPort: IProcessEdnMap,
            armorMap: Map<Any, Any>,
            defaultSource: String
        ): Armor {
            return Armor(
                0,
                processEdnMapPort.getValue(armorMap, ":name"),
                defaultSource,
                processEdnMapPort.getValue(armorMap, ":type"),
                processEdnMapPort.getValueOrDefault<Long?>(armorMap, ":base-ac", null)?.toInt(),
                processEdnMapPort.getValueOrDefault<Long?>(armorMap, ":max-dex-mod", null)?.toInt(),
                processEdnMapPort.getValueOrDefault(armorMap, ":stealth-disadvantage", null),
                processEdnMapPort.getValueOrDefault<Long?>(armorMap, ":weight", null)?.toInt(),
                processEdnMapPort.getValueOrDefault<Long?>(armorMap, ":min-str", null)?.toInt()
            )
        }
    }
}

