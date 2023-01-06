package com.feko.generictabletoprpg.ammunition

import com.feko.generictabletoprpg.common.Cost
import com.feko.generictabletoprpg.common.FromSource
import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.Named
import com.feko.generictabletoprpg.import.ProcessEdnMapPort

data class Ammunition(
    override val id: Long = 0,
    override val name: String,
    val sellQuantity: Long,
    val cost: Cost,
    val weight: String,
    override var source: String = ""
) : Identifiable,
    Named,
    FromSource {
    companion object {
        fun createFromOrcbrewData(
            processEdnMapPort: ProcessEdnMapPort,
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