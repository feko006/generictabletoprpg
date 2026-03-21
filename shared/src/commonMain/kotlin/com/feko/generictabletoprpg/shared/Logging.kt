package com.feko.generictabletoprpg.shared

interface ILogger {
    fun debug(tag: String? = null, message: () -> String)
    fun error(throwable: Throwable? = null, tag: String? = null, message: () -> String)
}

expect val logger: ILogger