package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common

import com.feko.generictabletoprpg.common.CoreConvertible
import com.feko.generictabletoprpg.common.Cost

data class CostEmbeddedEntity(
    val number: Long,
    val type: String
) : CoreConvertible<Cost> {
    companion object {
        fun fromCoreModel(item: Cost): CostEmbeddedEntity =
            CostEmbeddedEntity(item.number, item.type)
    }

    override fun toCoreModel(): Cost = Cost(number, type)
}