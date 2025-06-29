package com.feko.generictabletoprpg.common.data.local.entities

import androidx.annotation.Keep
import com.feko.generictabletoprpg.common.domain.model.Cost
import com.feko.generictabletoprpg.common.domain.model.ICoreConvertible

@Keep
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