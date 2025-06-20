package com.feko.generictabletoprpg.common.data.local

interface IInsertOrUpdateDao<in T> {
    fun insertOrUpdate(item: T): Long
}