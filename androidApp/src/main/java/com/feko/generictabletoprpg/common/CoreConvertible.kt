package com.feko.generictabletoprpg.common

interface CoreConvertible<T> {
    fun toCoreModel(): T
}