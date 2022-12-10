package com.feko.generictabletoprpg

import android.app.Application
import com.feko.generictabletoprpg.di.diModules
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class GenericTabletopRpgApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            modules(diModules)
        }
    }
}