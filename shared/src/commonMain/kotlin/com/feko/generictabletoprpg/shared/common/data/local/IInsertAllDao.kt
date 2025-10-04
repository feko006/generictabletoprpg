package com.feko.generictabletoprpg.shared.common.data.local

interface IInsertAllDao<in T> {
    fun insertAll(list: List<@JvmSuppressWildcards T>): Result<Boolean>
}
