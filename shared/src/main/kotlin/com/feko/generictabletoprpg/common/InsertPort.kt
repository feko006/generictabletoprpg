package com.feko.generictabletoprpg.common

interface InsertPort<in T> {
    fun insertAll(list: List<@JvmSuppressWildcards T>): Result<Boolean>
}
