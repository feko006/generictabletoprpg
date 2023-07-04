package com.feko.generictabletoprpg.common

interface GetAllByParentPort<out T> {
    fun getAllSortedByName(parentId: Long): List<T>
}