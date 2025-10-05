package com.feko.generictabletoprpg.shared.common.data.local

interface IGetAllDao<out T> {
    suspend fun getAllSortedByName(): List<T>
}