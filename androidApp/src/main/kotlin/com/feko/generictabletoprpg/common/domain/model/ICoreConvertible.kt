package com.feko.generictabletoprpg.common.domain.model

interface ICoreConvertible<T> {
    fun toCoreModel(): T
}