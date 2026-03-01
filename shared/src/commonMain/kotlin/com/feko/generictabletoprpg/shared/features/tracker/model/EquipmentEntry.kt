package com.feko.generictabletoprpg.shared.features.tracker.model

import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import kotlinx.serialization.Serializable

@Serializable
@DoNotObfuscate
data class EquipmentEntry(
    val item: IEquipmentItem,
    val count: Int = 1,
    val isFavorite: Boolean = false
)