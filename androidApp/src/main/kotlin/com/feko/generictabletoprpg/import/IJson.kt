package com.feko.generictabletoprpg.import

import java.lang.reflect.Type

interface IJson {
    fun <T> from(content: String, type: Type): T
    fun <T> to(data: T, type: Type): String
}
