package com.feko.generictabletoprpg.shared.common.data.local

interface IGetAllByParentDao<out T> {
    suspend fun getAllSortedByName(parentId: Long): List<T>
}