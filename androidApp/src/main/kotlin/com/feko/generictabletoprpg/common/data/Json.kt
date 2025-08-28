package com.feko.generictabletoprpg.common.data

import kotlinx.serialization.json.Json

val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
    explicitNulls = false
}
