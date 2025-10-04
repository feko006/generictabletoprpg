package com.feko.generictabletoprpg.shared.common.data.local

interface IGetAllByParentSortedByIndexDao<out T> {
    fun getAllSortedByIndex(parentId: Long): List<T>
}