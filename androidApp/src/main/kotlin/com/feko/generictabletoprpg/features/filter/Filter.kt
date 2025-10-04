package com.feko.generictabletoprpg.features.filter

import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.common.ui.components.appNamesByType

sealed class Filter(
    val type: Class<*>,
    val name: String? = null
) {
    open val chipData: List<FilterChipData> =
        listOf(
            FilterChipData(
                R.string.type.asText(),
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
