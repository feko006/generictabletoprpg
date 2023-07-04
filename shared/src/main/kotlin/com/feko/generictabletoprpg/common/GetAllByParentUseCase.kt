package com.feko.generictabletoprpg.common

interface GetAllByParentUseCase<out T> {
    fun getAll(parentId: Long): List<T>
}