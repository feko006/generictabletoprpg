package com.feko.generictabletoprpg.common.data.local

interface IGetAllDao<out T> {
    fun getAllSortedByName(): List<T>
}