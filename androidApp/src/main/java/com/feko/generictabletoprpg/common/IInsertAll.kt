package com.feko.generictabletoprpg.common

interface IInsertAll<in T> {
    fun insertAll(list: List<@JvmSuppressWildcards T>): Result<Boolean>
}
