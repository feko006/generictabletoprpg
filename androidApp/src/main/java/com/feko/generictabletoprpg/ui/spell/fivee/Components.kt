package com.feko.generictabletoprpg.ui.spell.fivee

data class Components(
    var v: Boolean? = null,
    var s: Boolean? = null,
    var m: MaterialComponents? = null
) {
    fun any(): Boolean {
        return v == true
                || s == true
                || m != null
    }

    override fun toString(): String {
        val builder = StringBuilder()
        if (v == true) {
            builder.append("V")
        }
        if (s == true) {
            if (builder.count() > 0) {
                builder.append(", ")
            }
            builder.append("S")
        }
        if (m != null) {
            if (builder.count() > 0) {
                builder.append(", ")
            }
            builder.append("M (${m.toString()})")
        }
        return builder.toString()
    }


}