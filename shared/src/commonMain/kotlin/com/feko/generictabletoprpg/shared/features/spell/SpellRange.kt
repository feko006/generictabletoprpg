package com.feko.generictabletoprpg.shared.features.spell

import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import kotlinx.serialization.Serializable

@DoNotObfuscate
@Serializable
data class SpellRange(
    val isSelf: Boolean,
    val isTouch: Boolean,
    val isSight: Boolean,
    val distance: Long,
    val unit: String?
) {
    companion object {
        fun createFromString(string: String): SpellRange {
            val parts = string.split(" ")
            return SpellRange(
                parts.first().lowercase().contains("self"),
                parts.first().lowercase().contains("touch"),
                parts.first().lowercase().contains("sight"),
                parts.first().toLongOrNull() ?: 0,
                parts.getOrNull(1)
            )
        }

        val Empty = SpellRange(false, false, false, 0L, null)
    }

    override fun toString(): String {
        return if (isSelf) {
            "Self"
        } else if (isTouch) {
            "Touch"
        } else if (isSight) {
            "Sight"
        } else {
            "$distance $unit"
        }
    }
}