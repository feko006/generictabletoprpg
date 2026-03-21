package com.feko.generictabletoprpg.shared

import com.russhwolf.settings.Settings
import org.jetbrains.compose.resources.StringResource

expect val settings: Settings
expect val fileImportHint: StringResource

expect suspend fun loadResourceAsBytes(path: String): ByteArray
expect suspend fun loadResourceAsString(path: String): String
expect fun preprocessFileShortcut(path: String): String