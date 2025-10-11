package com.feko.generictabletoprpg.shared

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual fun platform() = "Android"

lateinit var appContext: Context // initialize this early in your app

actual suspend fun loadResourceAsBytes(path: String): ByteArray {
    val inputStream = appContext.assets.open(path)
    return inputStream.readBytes()
}

actual suspend fun loadResourceAsString(path: String): String {
    val inputStream = appContext.assets.open(path)
    return inputStream.bufferedReader().use { it.readText() }
}

private val sharedPreferences =
    appContext.getSharedPreferences("gttrpg-prefs", Context.MODE_PRIVATE)
actual val settings: Settings = SharedPreferencesSettings(sharedPreferences, commit = true)