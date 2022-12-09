package com.feko.generictabletoprpg.ui.spell.fivee

data class Distance(
    var type: String? = null,
    var amount: Int? = null
) {
    override fun toString(): String {
        return "$amount $type"
    }
}