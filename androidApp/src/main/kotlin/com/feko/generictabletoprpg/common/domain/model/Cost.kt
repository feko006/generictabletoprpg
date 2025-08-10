package com.feko.generictabletoprpg.common.domain.model

import androidx.annotation.Keep
import com.feko.generictabletoprpg.common.domain.IProcessEdnMap
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Cost(
    val number: Long,
    val type: String
) {
    override fun toString(): String = "$number $type"

    companion object {
        fun createFromOrcbrewData(
            processEdnMapPort: IProcessEdnMap,
            featMap: Map<Any, Any>
        ): Cost {
            val type =
                processEdnMapPort.getValue<Any>(featMap, ":type")
                    .toString()
                    .substring(1)
            return Cost(processEdnMapPort.getValue(featMap, ":num"), type)
        }
    }
}