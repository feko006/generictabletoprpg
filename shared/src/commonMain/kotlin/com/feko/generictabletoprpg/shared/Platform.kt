package com.feko.generictabletoprpg.shared

import com.russhwolf.settings.Settings

expect fun platform(): String

expect suspend fun loadResourceAsBytes(path: String): ByteArray
expect suspend fun loadResourceAsString(path: String): String

expect val settings: Settings