package com.feko.generictabletoprpg.common

interface IGetAllByParentSortedByIndex<out T> {
    fun getAllSortedByIndex(parentId: Long): List<T>
}