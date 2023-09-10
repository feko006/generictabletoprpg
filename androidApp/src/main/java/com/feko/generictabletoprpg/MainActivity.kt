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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feko.generictabletoprpg.destinations.ImportScreenDestination
import com.feko.generictabletoprpg.destinations.SearchAllScreenDestination
import com.feko.generictabletoprpg.destinations.TrackerGroupsScreenDestination
import com.feko.generictabletoprpg.theme.GenerictabletoprpgTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.navigate
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
                                                    Icon(it.icon, "")
                                                }
                                            }
                                    })
                            }) { paddingValues ->
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
    val activeDrawerItem = rememberSaveable { mutableStateOf("") }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                val drawerItemData = listOf(
                    Pair("Search All", SearchAllScreenDestination()),
                    Pair("Tracker", TrackerGroupsScreenDestination()),
                    Pair("Import", ImportScreenDestination())
                )
                drawerItemData.forEach { (screenTitle, direction) ->
                    NavigationDrawerItem(
                        label = { Text(screenTitle) },
                        selected = activeDrawerItem.value == direction.route,
                        onClick = {
                            activeDrawerItem.value = direction.route
                            scope.launch {
                                drawerState.close()
                            }
                            navController.navigate(direction) {
                                popUpTo(navController.graph.findStartDestination().id) {
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
