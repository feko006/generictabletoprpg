package com.feko.generictabletoprpg.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgApp
import com.feko.generictabletoprpg.shared.common.ui.theme.GttrpgTheme
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(diModules)
    }
    return application {
        Window(onCloseRequest = ::exitApplication, title = "gttrpg") {
            GttrpgTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) { GttrpgApp() }
            }
        }
    }
}
