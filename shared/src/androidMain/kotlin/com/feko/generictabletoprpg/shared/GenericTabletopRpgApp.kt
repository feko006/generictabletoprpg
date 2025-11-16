package com.feko.generictabletoprpg.shared

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import org.koin.core.context.startKoin

class GenericTabletopRpgApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
        startKoin {
            modules(diModules)
        }
    }
}