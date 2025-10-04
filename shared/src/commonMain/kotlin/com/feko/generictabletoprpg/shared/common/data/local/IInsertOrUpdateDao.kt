package com.feko.generictabletoprpg.shared.common.data.local

interface IInsertOrUpdateDao<in T> {
    fun insertOrUpdate(item: T): Long
}