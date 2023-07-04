package com.feko.generictabletoprpg.common

interface GetAllPort<out T> {
    fun getAllSortedByName(): List<T>
}