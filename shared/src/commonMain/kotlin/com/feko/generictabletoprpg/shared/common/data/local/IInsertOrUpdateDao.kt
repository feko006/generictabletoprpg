package com.feko.generictabletoprpg.shared.common.data.local

interface IInsertOrUpdateDao<in T> {
    suspend fun insertOrUpdate(item: T): Long
}