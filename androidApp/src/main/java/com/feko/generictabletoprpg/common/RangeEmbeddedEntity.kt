package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common

import com.feko.generictabletoprpg.common.Range

data class RangeEmbeddedEntity(
    val isSelf: Boolean,
    val isTouch: Boolean,
    val isSight: Boolean,
    val distance: Long,
    val unit: String?
) {
    companion object {
        fun fromCoreModel(range: Range) =
            RangeEmbeddedEntity(
                range.isSelf,
                range.isTouch,
                range.isSight,
                range.distance,
                range.unit
            )
    }
}