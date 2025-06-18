package com.feko.generictabletoprpg

import android.app.Application
import com.feko.generictabletoprpg.di.diModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class GenericTabletopRpgApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GenericTabletopRpgApp)
            modules(diModules)
        }

        Timber.plant(Timber.DebugTree())
    }
}