package com.feko.generictabletoprpg.common

interface GetAllUseCase<out T> {
    fun getAll(): List<T>
}