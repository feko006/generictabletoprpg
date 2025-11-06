package com.feko.generictabletoprpg.shared.common.data

import com.feko.generictabletoprpg.shared.common.domain.IProcessEdnMap
import us.bpsm.edn.Keyword

class ProcessEdnMapEdnJava : IProcessEdnMap {
    override fun <T> getValue(map: Map<Any, Any>, key: String): T {
        val actualKey = map.keys
            .filterIsInstance<Keyword>()
            .first { it.toString() == key }
        @Suppress("UNCHECKED_CAST")
        return map[actualKey] as T
    }

    override fun <T> getValueOrDefault(map: Map<Any, Any>, key: String, defaultValue: T): T {
        val actualKey = map.keys
            .filterIsInstance<Keyword>()
            .firstOrNull { it.toString() == key }
            ?: return defaultValue
        @Suppress("UNCHECKED_CAST")
        return map[actualKey] as T
    }

    override fun containsKey(map: Map<Any, Any>, key: String): Boolean =
        map.keys
            .filterIsInstance<Keyword>()
            .any { it.toString() == key }

    override fun <T> toStringKeyedMap(map: Map<Any, Any>): Map<String, T> {
        val result = mutableMapOf<String, T>()
        map.forEach {
            @Suppress("UNCHECKED_CAST")
            result[it.key.toString()] = it.value as T
        }
        return result
    }
}