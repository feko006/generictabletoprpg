package com.feko.generictabletoprpg.common

interface GetByIdUseCase<out T> {
    fun getById(id: Long): T
}
