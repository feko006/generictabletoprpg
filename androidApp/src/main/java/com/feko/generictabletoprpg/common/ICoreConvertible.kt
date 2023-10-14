package com.feko.generictabletoprpg.common

interface ICoreConvertible<T> {
    fun toCoreModel(): T
}