package com.feko.generictabletoprpg.shared.common.data.local

interface IGetByIdDao<out T> {
    suspend fun getById(id: Long): T
}
