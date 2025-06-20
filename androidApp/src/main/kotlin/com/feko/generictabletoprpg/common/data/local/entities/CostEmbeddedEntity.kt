package com.feko.generictabletoprpg.common.data.local.entities

import com.feko.generictabletoprpg.common.domain.model.Cost
import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.common.domain.model.ICoreConvertible

@DoNotObfuscate
data class CostEmbeddedEntity(
    val number: Long,
    val type: String
) : ICoreConvertible<Cost> {
    companion object {
        fun fromCoreModel(item: Cost): CostEmbeddedEntity =
            CostEmbeddedEntity(item.number, item.type)
    }

    override fun toCoreModel(): Cost = Cost(number, type)
}