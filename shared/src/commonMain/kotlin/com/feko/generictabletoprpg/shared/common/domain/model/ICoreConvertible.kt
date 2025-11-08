package com.feko.generictabletoprpg.shared.common.domain.model

interface ICoreConvertible<T> {
    fun toCoreModel(): T
}