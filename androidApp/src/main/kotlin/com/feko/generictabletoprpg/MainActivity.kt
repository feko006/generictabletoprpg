package com.feko.generictabletoprpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feko.generictabletoprpg.theme.GenerictabletoprpgTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.utils.navGraph
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GenerictabletoprpgTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) { GttrpgApp() }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun GttrpgApp() {
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

            is AppViewModel.AppState.ShowingScreen -> {
                val currentAppState = appState as AppViewModel.AppState.ShowingScreen
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(currentAppState.appBarTitle)
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
                            }, actions = {
                                currentAppState
                                    .navBarActions
                                    .forEach {
                                        IconButton(
                                            onClick = it.onClick
                                        ) {
                                            if (it.icon != null) {
                                                Icon(it.icon, "")
                                            } else {
                                                requireNotNull(it.painter)
                                                Icon(it.painter, "")
                                            }
                                        }
                                    }
                            })
                    }) { paddingValues ->
                    if (drawerState.isOpen) {
                        BackHandler {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }
                    }
                    NavigationDrawer(
                        drawerState,
                        paddingValues,
                        navController,
                        appViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    paddingValues: PaddingValues,
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    val scope = rememberCoroutineScope()
    val activeDrawerItemRoute = appViewModel.activeDrawerItemRoute.collectAsState()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                RootDestinations.entries.forEach { rootDestination ->
                    NavigationDrawerItem(
                        label = { Text(stringResource(rootDestination.title)) },
                        selected = activeDrawerItemRoute.value == rootDestination.direction.route,
                        onClick = {
                            appViewModel.updateActiveDrawerItem(rootDestination)
                            scope.launch {
                                drawerState.close()
                            }
                            navController.toDestinationsNavigator()
                                .navigate(rootDestination.direction) {
                                    popUpTo(navController.navGraph.defaultStartDirection) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                        })
                }
            }
        },
        modifier = Modifier.padding(paddingValues)
    ) {
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            navController = navController,
            dependenciesContainerBuilder = {
                dependency(appViewModel)
            }
        )
    }
}
