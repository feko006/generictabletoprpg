package com.feko.generictabletoprpg.shared.common.data

import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
    explicitNulls = false
}
