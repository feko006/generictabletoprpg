package com.feko.generictabletoprpg.shared.features.filter

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.shared.common.appNamesByType
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.type
import kotlin.reflect.KClass

sealed class Filter(
    val type: KClass<*>?,
    val name: String? = null
) {
    open val chipData: List<FilterChipData> =
        buildList {
            type?.let {
                val typeChip =
                    FilterChipData(
                        Res.string.type.asText(),
                        appNamesByType[it]!!.asText(),
                        null
                    )
                add(typeChip)
            }
        }

    open fun isAccepted(obj: Any, type: KClass<*>? = null): Boolean {
        var isAccepted = true
        (type ?: this.type)?.run {
            isAccepted = isInstance(obj)
        }
        if (name != null && obj is INamed) {
            isAccepted = isAccepted && obj.name.lowercase().contains(name.lowercase())
        }
        return isAccepted
    }
}
