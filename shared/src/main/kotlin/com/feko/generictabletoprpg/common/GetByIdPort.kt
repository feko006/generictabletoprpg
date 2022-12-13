package com.feko.generictabletoprpg.common

interface GetByIdPort<out T> {
    fun getById(id: Long): T
}
