package com.feko.generictabletoprpg.shared.features.filter

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.shared.common.appNamesByType
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.type

sealed class Filter(
    val type: Class<*>,
    val name: String? = null
) {
    open val chipData: List<FilterChipData> =
        listOf(
            FilterChipData(
                Res.string.type.asText(),
                appNamesByType[type]!!.asText(),
                null
            )
        )

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
