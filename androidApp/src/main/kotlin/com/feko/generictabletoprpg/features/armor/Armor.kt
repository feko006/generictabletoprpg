package com.feko.generictabletoprpg.features.armor

import androidx.annotation.Keep
import com.feko.generictabletoprpg.common.domain.IProcessEdnMap
import com.feko.generictabletoprpg.common.domain.model.IFromSource
import com.feko.generictabletoprpg.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed
import kotlinx.serialization.Serializable

@Keep
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
    IFromSource {
    val weightInLbs
        get() = "$weight lbs"

    companion object {
        fun createFromOrcbrewData(
            processEdnMapPort: IProcessEdnMap,
            armorMap: Map<Any, Any>,
            defaultSource: String
        ): Armor {
            val type = processEdnMapPort.getValue<Any>(armorMap, ":type")
                .toString()
                .substring(1)
            return Armor(
                0,
                processEdnMapPort.getValue(armorMap, ":name"),
                defaultSource,
                type,
                processEdnMapPort.getValueOrDefault<Long?>(armorMap, ":base-ac", null)?.toInt(),
                processEdnMapPort.getValueOrDefault<Long?>(armorMap, ":max-dex-mod", null)?.toInt(),
                processEdnMapPort.getValueOrDefault(armorMap, ":stealth-disadvantage", null),
                processEdnMapPort.getValueOrDefault<Long?>(armorMap, ":weight", null)?.toInt(),
                processEdnMapPort.getValueOrDefault<Long?>(armorMap, ":min-str", null)?.toInt()
            )
        }
    }
}

