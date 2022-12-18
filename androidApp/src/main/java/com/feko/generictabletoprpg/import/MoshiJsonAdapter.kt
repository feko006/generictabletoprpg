package com.feko.generictabletoprpg.import

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiJsonAdapter : JsonPort {
    private val moshiJson =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Suppress("UNCHECKED_CAST")
    override fun <T> from(content: String, type: Class<T>): T =
        moshiJson.adapter(type).fromJson(content) as T

    override fun <T> to(data: T, type: Class<T>): String =
        moshiJson.adapter(type).toJson(data)
}