package com.feko.generictabletoprpg.common

import timber.log.Timber

class TimberLogger : Logger {
    override fun error(e: Exception, message: String) {
        Timber.e(e, message)
    }
}