package com.feko.generictabletoprpg.shared.features.ammunition

import com.feko.generictabletoprpg.shared.common.domain.IProcessEdnMap
import com.feko.generictabletoprpg.shared.common.domain.model.Cost
import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.common.domain.model.IFromSource
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import kotlinx.serialization.Serializable

@DoNotObfuscate
@Serializable
data class Ammunition(
    override val id: Long = 0,
    override val name: String,
    val sellQuantity: Long,
    val cost: Cost,
    val weight: String,
    override var source: String = ""
) : IIdentifiable,
    INamed,
    IFromSource {
    companion object {
        fun createFromOrcbrewData(
            processEdnMapPort: IProcessEdnMap,
            ammunitionMap: Map<Any, Any>,
            defaultSource: String
        ): Ammunition {
            return Ammunition(
                0,
                processEdnMapPort.getValue(ammunitionMap, ":name"),
                processEdnMapPort.getValue(ammunitionMap, ":sell-qty"),
                Cost.createFromOrcbrewData(
                    processEdnMapPort,
                    processEdnMapPort.getValue(ammunitionMap, ":cost")
                ),
                processEdnMapPort.getValue(ammunitionMap, ":weight"),
                defaultSource
            )
        }
    }
}