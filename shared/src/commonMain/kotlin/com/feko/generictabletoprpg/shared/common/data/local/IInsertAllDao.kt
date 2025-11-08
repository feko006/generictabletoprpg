package com.feko.generictabletoprpg.shared.common.data.local

interface IInsertAllDao<in T> {
    suspend fun insertAll(list: List<T>): Result<Boolean>
}
