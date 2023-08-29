package com.feko.generictabletoprpg.common

interface IInsertOrUpdate<in T> {
    fun insertOrUpdate(item: T): Long
}