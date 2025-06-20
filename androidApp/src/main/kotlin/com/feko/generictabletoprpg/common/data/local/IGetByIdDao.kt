package com.feko.generictabletoprpg.common.data.local

interface IGetByIdDao<out T> {
    fun getById(id: Long): T
}
