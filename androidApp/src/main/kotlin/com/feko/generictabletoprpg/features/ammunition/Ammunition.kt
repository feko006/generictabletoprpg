package com.feko.generictabletoprpg.features.ammunition

import com.feko.generictabletoprpg.common.domain.model.Cost
import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.common.domain.model.IFromSource
import com.feko.generictabletoprpg.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed
import com.feko.generictabletoprpg.common.domain.IProcessEdnMap

@DoNotObfuscate
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