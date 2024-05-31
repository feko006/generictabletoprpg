package com.feko.generictabletoprpg.import

import java.lang.reflect.Type

interface IJson {
    fun <T> from(content: String, type: Class<T>): T
    fun <T> from(content: String, type: Type): T
    fun <T> to(data: T, type: Class<T>): String
    fun <T> to(data: T, type: Type): String
}
