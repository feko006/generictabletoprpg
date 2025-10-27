package com.feko.generictabletoprpg.shared

import android.app.Application
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GenericTabletopRpgApp : Application() {
    override fun onCreate() {
        super.onCreate()

        appContext = this

        startKoin {
            androidLogger()
            modules(diModules)
        }
    }
}