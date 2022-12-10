package com.feko.generictabletoprpg.spell.fiveetools

sealed class MaterialComponents {
    data class FullMaterialComponents(
        val text: String? = null,
        val cost: Int? = null,
        val consume: Any? = null
    ) : MaterialComponents() {
        override fun toString(): String {
            val builder = StringBuilder(text!!)
            cost?.let {
                builder.append(", costing at least $cost gp")
            }
            consume?.let {
                builder.append(", consumed upon use")
            }
            if (builder.last() != '.') {
                builder.append(".")
            }
            return builder.toString()
        }
    }

    data class StringMaterialComponents(val text: String? = null) : MaterialComponents() {
        override fun toString(): String {
            return text!!
        }
    }
}