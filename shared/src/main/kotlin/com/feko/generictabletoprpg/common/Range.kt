package com.feko.generictabletoprpg.common

data class Range(
    val isSelf: Boolean,
    val isTouch: Boolean,
    val isSight: Boolean,
    val distance: Long,
    val unit: String?
) {
    companion object {
        fun createFromString(string: String): Range {
            val parts = string.split(" ")
            return Range(
                parts.first().lowercase().contains("self"),
                parts.first().lowercase().contains("touch"),
                parts.first().lowercase().contains("sight"),
                parts.first().toLongOrNull() ?: 0,
                parts.getOrNull(1)
            )
        }
    }
}