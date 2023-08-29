package com.feko.generictabletoprpg.common

interface IGetById<out T> {
    fun getById(id: Long): T
}
