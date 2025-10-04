package com.feko.generictabletoprpg.shared.common.data.local

interface IGetByIdDao<out T> {
    fun getById(id: Long): T
}
