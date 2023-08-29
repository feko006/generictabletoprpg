package com.feko.generictabletoprpg.import

interface IJson {
    fun <T> from(content: String, type: Class<T>): T
    fun <T> to(data: T, type: Class<T>): String
}
