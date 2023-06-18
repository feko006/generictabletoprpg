package com.feko.generictabletoprpg.common

interface InsertOrUpdatePort<in T> {
    fun insertOrUpdate(item: T): Long
}