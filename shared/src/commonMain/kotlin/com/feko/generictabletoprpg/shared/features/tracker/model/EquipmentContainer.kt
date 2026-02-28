package com.feko.generictabletoprpg.shared.features.tracker.model

import kotlinx.serialization.Serializable

@Serializable
data class EquipmentContainer(
    val entries: List<IEquipmentListEntry>
) {

    companion object {
        val Empty = EquipmentContainer(listOf())
    }
}