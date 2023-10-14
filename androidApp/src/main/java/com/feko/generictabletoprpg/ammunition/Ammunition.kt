package com.feko.generictabletoprpg.ammunition

import com.feko.generictabletoprpg.common.Cost
import com.feko.generictabletoprpg.common.IFromSource
import com.feko.generictabletoprpg.common.IIdentifiable
import com.feko.generictabletoprpg.common.INamed
import com.feko.generictabletoprpg.import.IProcessEdnMap

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