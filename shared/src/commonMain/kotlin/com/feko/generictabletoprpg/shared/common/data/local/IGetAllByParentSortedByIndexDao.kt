package com.feko.generictabletoprpg.shared.common.data.local

import kotlinx.coroutines.flow.Flow

interface IGetAllByParentSortedByIndexDao<out T> {
    fun getAllSortedByIndex(parentId: Long): Flow<List<T>>
}