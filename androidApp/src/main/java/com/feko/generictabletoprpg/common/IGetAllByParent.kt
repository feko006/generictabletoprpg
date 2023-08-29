package com.feko.generictabletoprpg.common

interface IGetAllByParent<out T> {
    fun getAllSortedByName(parentId: Long): List<T>
}