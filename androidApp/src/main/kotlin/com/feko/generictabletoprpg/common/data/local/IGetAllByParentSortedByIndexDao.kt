package com.feko.generictabletoprpg.common.data.local

interface IGetAllByParentSortedByIndexDao<out T> {
    fun getAllSortedByIndex(parentId: Long): List<T>
}