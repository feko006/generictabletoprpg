package com.feko.generictabletoprpg.shared.features.tracker.model

import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.common.domain.model.IGuidIdentifiable
import kotlinx.serialization.Serializable

@Serializable
@DoNotObfuscate
data class FileShortcutsContainer(
    val entries: List<FileShortcutEntry>
) {
    companion object {
        val Empty = FileShortcutsContainer(listOf())
    }
}

@Serializable
@DoNotObfuscate
data class FileShortcutEntry(
    override val id: String,
    val name: String,
    val path: String
) : IGuidIdentifiable {
    fun isValid() = name.isNotEmpty()
}