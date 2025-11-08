package com.feko.generictabletoprpg.shared

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

actual fun platform(): String = "jvm"

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

private val preferences = Preferences.userRoot().node("gttrpg-prefs")
actual val settings: Settings = PreferencesSettings(preferences)