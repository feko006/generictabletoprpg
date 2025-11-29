package com.feko.generictabletoprpg.shared

import android.content.Context
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.file_import_hint_android
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.jetbrains.compose.resources.StringResource

actual val settings: Settings by lazy {
    SharedPreferencesSettings(sharedPreferences, commit = true)
}

actual val fileImportHint: StringResource = Res.string.file_import_hint_android

lateinit var appContext: Context

actual suspend fun loadResourceAsBytes(path: String): ByteArray {
    val inputStream = appContext.assets.open(path)
    return inputStream.readBytes()
}

actual suspend fun loadResourceAsString(path: String): String {
    val inputStream = appContext.assets.open(path)
    return inputStream.bufferedReader().use { it.readText() }
}

private val sharedPreferences by lazy {
    appContext.getSharedPreferences("gttrpg-prefs", Context.MODE_PRIVATE)
}