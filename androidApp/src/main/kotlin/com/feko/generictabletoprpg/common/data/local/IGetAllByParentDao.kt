package com.feko.generictabletoprpg.common.data.local

interface IGetAllByParentDao<out T> {
    fun getAllSortedByName(parentId: Long): List<T>
}