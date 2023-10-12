package com.feko.generictabletoprpg.common

interface ILogger {
    fun error(e: Exception, message: String)
    fun debug(message: String)
}