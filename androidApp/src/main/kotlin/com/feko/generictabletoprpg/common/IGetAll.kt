package com.feko.generictabletoprpg.common

interface IGetAll<out T> {
    fun getAllSortedByName(): List<T>
}