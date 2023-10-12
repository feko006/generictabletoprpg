package com.feko.generictabletoprpg.common

import timber.log.Timber

class TimberLogger : ILogger {
    override fun error(e: Exception, message: String) {
        Timber.e(e, message)
    }

    override fun debug(message: String) {
        Timber.d(message)
    }
}