package com.feko.generictabletoprpg.common.domain

interface IProcessEdnMap {
    fun <T> getValue(map: Map<Any, Any>, key: String): T
    fun <T> getValueOrDefault(map: Map<Any, Any>, key: String, defaultValue: T): T
    fun containsKey(map: Map<Any, Any>, key: String): Boolean
    fun <T> toStringKeyedMap(map: Map<Any, Any>): Map<String, T>
}
