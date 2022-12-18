package com.feko.generictabletoprpg.common

interface InsertAllPort<in T> {
    fun insertAll(list: List<@JvmSuppressWildcards T>): Result<Boolean>
}
