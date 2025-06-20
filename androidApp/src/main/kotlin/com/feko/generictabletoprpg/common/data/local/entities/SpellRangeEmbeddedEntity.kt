package com.feko.generictabletoprpg.common.data.local.entities

import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.features.spell.SpellRange

@DoNotObfuscate
data class SpellRangeEmbeddedEntity(
    val isSelf: Boolean,
    val isTouch: Boolean,
    val isSight: Boolean,
    val distance: Long,
    val unit: String?
) {
    fun toCoreModel(): SpellRange =
        SpellRange(
            isSelf,
            isTouch,
            isSight,
            distance,
            unit
        )

    companion object {
        fun fromCoreModel(range: SpellRange) =
            SpellRangeEmbeddedEntity(
                range.isSelf,
                range.isTouch,
                range.isSight,
                range.distance,
                range.unit
            )
    }
}