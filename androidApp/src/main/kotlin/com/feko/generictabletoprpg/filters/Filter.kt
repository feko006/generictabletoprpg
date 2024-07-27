package com.feko.generictabletoprpg.filters

import com.feko.generictabletoprpg.common.INamed

sealed class Filter(
    val type: Class<*>,
    val name: String? = null
) {
    open fun isAccepted(obj: Any): Boolean {
        var isAccepted = obj::class.java == type
        if (name != null) {
            isAccepted = isAccepted
                    && obj is INamed
                    && obj.name.lowercase().contains(name.lowercase())
        }
        return isAccepted
    }
}
