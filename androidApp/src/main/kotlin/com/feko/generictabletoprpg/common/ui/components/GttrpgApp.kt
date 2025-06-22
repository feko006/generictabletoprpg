package com.feko.generictabletoprpg.common.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.feko.generictabletoprpg.common.ui.RootDestinations
import com.feko.generictabletoprpg.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.utils.navGraph
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GttrpgApp() {
    val appViewModel: AppViewModel = koinViewModel()
    val appState by appViewModel.appState.collectAsState()

    when (appState) {
        is AppViewModel.AppState.ImportingBaseContent -> FillingLoadingIndicator()


        is AppViewModel.AppState.ShowingScreen -> {
            val activeDrawerItemRoute = appViewModel.activeDrawerItemRoute.collectAsState()
            val scope = rememberCoroutineScope()
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            if (drawerState.isOpen) {
                BackHandler { scope.launch { drawerState.close() } }
            }
            val navController = rememberNavController()
            ModalNavigationDrawer(
                drawerContent = {
                    ModalDrawerSheet {
                        Column(
                            Modifier.padding(LocalDimens.current.paddingSmall)
                        ) {
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
                    }
                },
                Modifier.safeDrawingPadding(),
                drawerState
            ) {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    navController = navController,
                    dependenciesContainerBuilder = {
                        dependency(appViewModel)
                        dependency {
                            scope.launch {
                                drawerState.apply {
                                    if (isOpen) close() else open()
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}