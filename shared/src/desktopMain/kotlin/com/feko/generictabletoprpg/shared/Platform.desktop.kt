package com.feko.generictabletoprpg.shared

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.file_import_hint_desktop
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.jetbrains.compose.resources.StringResource
import java.util.prefs.Preferences

private val preferences = Preferences.userRoot().node("gttrpg-prefs")
actual val settings: Settings = PreferencesSettings(preferences)
actual val fileImportHint: StringResource = Res.string.file_import_hint_desktop

actual suspend fun loadResourceAsBytes(path: String): ByteArray {
    val stream = object {}.javaClass.classLoader.getResourceAsStream(path)
        ?: error("Resource not found: $path")
    return stream.readBytes()
}

actual suspend fun loadResourceAsString(path: String): String {
    val stream = object {}.javaClass.classLoader.getResourceAsStream(path)
        ?: error("Resource not found: $path")

    return stream.bufferedReader().use { it.readText() }
}