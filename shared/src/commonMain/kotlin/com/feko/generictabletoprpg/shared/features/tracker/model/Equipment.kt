package com.feko.generictabletoprpg.shared.features.tracker.model

import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.common.domain.model.IGuidIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@DoNotObfuscate
data class EquipmentContainer(
    val entries: List<EquipmentEntry>
) {
    companion object {
        val Empty = EquipmentContainer(listOf())
    }
}

@Serializable
@DoNotObfuscate
data class EquipmentEntry(
    val item: IEquipmentItem,
    val count: Int = 1,
    val isFavorite: Boolean = false
)

interface IEquipmentItem : INamed

@Serializable
@DoNotObfuscate
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
