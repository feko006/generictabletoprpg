package com.feko.generictabletoprpg.spell

import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
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