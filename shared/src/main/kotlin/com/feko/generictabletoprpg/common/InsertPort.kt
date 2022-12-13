package com.feko.generictabletoprpg.common

interface InsertPort<in T> {
    fun insert(list: List<@JvmSuppressWildcards T>): Result<Boolean>
}
