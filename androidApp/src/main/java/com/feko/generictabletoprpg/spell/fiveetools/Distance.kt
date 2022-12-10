package com.feko.generictabletoprpg.spell.fiveetools

data class Distance(
    var type: String? = null,
    var amount: Int? = null
) {
    override fun toString(): String {
        return "$amount $type"
    }
}