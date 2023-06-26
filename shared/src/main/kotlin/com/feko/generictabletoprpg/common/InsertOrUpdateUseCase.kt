package com.feko.generictabletoprpg.common

interface InsertOrUpdateUseCase<in T> {
    fun insertOrUpdate(item: T): Long
}