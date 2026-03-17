package com.feko.generictabletoprpg.shared.features.tracker.model

import com.feko.generictabletoprpg.shared.common.domain.model.IGuidIdentifiable
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class EquipmentItem(
    override val id: String,
    override val name: String,
    val description: String
) : IEquipmentItem, IGuidIdentifiable {
    fun isValid() = isNameValid() and isDescriptionValid()

    fun isNameValid(): Boolean = name.isNotBlank()

    fun isDescriptionValid(): Boolean = description.isNotBlank()

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun empty() = EquipmentItem(Uuid.random().toHexDashString(), "", "")
    }
}