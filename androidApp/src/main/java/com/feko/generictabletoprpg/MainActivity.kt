package com.feko.generictabletoprpg

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.feko.generictabletoprpg.theme.GenerictabletoprpgTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var appContext: Context
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext

        setContent {
            GenerictabletoprpgTheme {
                val appViewModel: AppViewModel = koinViewModel()
                val appState by appViewModel.appState.collectAsState()

                when (appState) {
                    is AppViewModel.AppState.ImportingBaseContent -> {
                        Box(Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                Modifier
                                    .size(100.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    is AppViewModel.AppState.ReadyToUse -> {
                        val drawerState = rememberDrawerState(DrawerValue.Closed)
                        val coroutineScope = rememberCoroutineScope()
                        val navController = rememberNavController()
                        val appBarTitle = rememberSaveable { mutableStateOf("") }

                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = {
                                        Text(appBarTitle.value)
                                    },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = {
                                                coroutineScope.launch {
                                                    if (drawerState.isOpen) {
                                                        drawerState.close()
                                                    } else {
                                                        drawerState.open()
                                                    }
                                                }
                                            }) {
                                            Icon(Icons.Default.Menu, "")
                                        }
                                    })
                            }) { paddingValues ->
                            Navigation.Drawer(
                                drawerState,
                                paddingValues,
                                navController,
                                appBarTitle
                            )
                        }
                    }
                }
            }
        }
    }
}