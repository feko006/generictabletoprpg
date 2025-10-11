package com.feko.generictabletoprpg

import android.app.Application
import com.feko.generictabletoprpg.shared.appContext
import com.feko.generictabletoprpg.shared.diModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class GenericTabletopRpgApp : Application() {
    override fun onCreate() {
        super.onCreate()

        appContext = this

        startKoin {
            androidLogger()
            androidContext(this@GenericTabletopRpgApp)
            modules(diModules)
        }
    }
}