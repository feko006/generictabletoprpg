package com.feko.generictabletoprpg.shared.common.data.local

import kotlinx.coroutines.flow.Flow

interface IGetAllDao<out T> {
    fun getAllSortedByName(): Flow<List<T>>
}