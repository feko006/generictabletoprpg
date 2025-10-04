package com.feko.generictabletoprpg.shared.common.data.local

interface IGetAllDao<out T> {
    fun getAllSortedByName(): List<T>
}