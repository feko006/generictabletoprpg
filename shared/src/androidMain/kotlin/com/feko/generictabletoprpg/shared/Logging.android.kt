package com.feko.generictabletoprpg.shared

import logcat.LogPriority
import logcat.logcat

actual val logger: ILogger
    get() = object : ILogger {
        override fun debug(tag: String?, message: () -> String) {
            logcat(LogPriority.DEBUG, tag, message)
        }

        override fun error(
            throwable: Throwable?,
            tag: String?,
            message: () -> String
        ) {
            logcat(LogPriority.ERROR, tag, message)
            throwable?.let {
                logcat(LogPriority.ERROR) { it.stackTraceToString() }
            }
        }

    }