package com.feko.generictabletoprpg.common

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