package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationEventHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import androidx.savedstate.serialization.SavedStateConfiguration
import com.feko.generictabletoprpg.shared.common.ui.RootDestinations
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalComposeUiApi::class
)
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
                val eventState = rememberNavigationEventState(NavigationEventInfo.None)
                NavigationEventHandler(eventState, isForwardEnabled = false) {
                    scope.launch { drawerState.close() }
                }
            }
            val backStack = rememberNavBackStack(
                SavedStateConfiguration {
                    serializersModule = INavigationDestination.serializersModule
                },
                INavigationDestination.startDestination
            )
            val isBigScreen = LocalDimens.current.isBigScreen
            if (isBigScreen) {
                LargeScreenNavigation(
                    activeDrawerItemRoute,
                    appViewModel,
                    backStack,
                    scope,
                    drawerState
                )
            } else {
                SmallScreenNavigation(
                    activeDrawerItemRoute,
                    appViewModel,
                    scope,
                    drawerState,
                    backStack
                )
            }
        }
    }
}

@Composable
private fun LargeScreenNavigation(
    activeDrawerItemRoute: State<INavigationDestination>,
    appViewModel: AppViewModel,
    backStack: NavBackStack<NavKey>,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    Row {
        NavigationRail(Modifier.padding(LocalDimens.current.paddingSmall)) {
            val rootDestinations = RootDestinations.entries
            rootDestinations.forEachIndexed { index, rootDestination ->
                val isSelected =
                    activeDrawerItemRoute.value == rootDestination.destination
                NavigationRailItem(
                    icon = {
                        Icon(
                            rootDestination.selectedIcon.imageVector(),
                            stringResource(rootDestination.title)
                        )
                    },
                    label = { Text(stringResource(rootDestination.title)) },
                    selected = isSelected,
                    onClick = { updateRootDestination(appViewModel, rootDestination, backStack) })
            }
        }
        NavigationHost(scope, drawerState, backStack, appViewModel)
    }
}

@Composable
private fun SmallScreenNavigation(
    activeDrawerItemRoute: State<INavigationDestination>,
    appViewModel: AppViewModel,
    scope: CoroutineScope,
    drawerState: DrawerState,
    backStack: NavBackStack<NavKey>
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(drawerShape = MaterialTheme.shapes.extraLarge) {
                Column(Modifier.padding(LocalDimens.current.paddingSmall)) {
                    val rootDestinations = RootDestinations.entries
                    rootDestinations.forEach { rootDestination ->
                        NavigationDrawerItem(
                            label = { Text(stringResource(rootDestination.title)) },
                            selected = activeDrawerItemRoute.value == rootDestination.destination,
                            onClick = {
                                scope.launch { drawerState.close() }
                                updateRootDestination(appViewModel, rootDestination, backStack)
                            })
                    }
                }
            }
        },
        Modifier.safeDrawingPadding(),
        drawerState,
        gesturesEnabled = !drawerState.isAnimationRunning
    ) { NavigationHost(scope, drawerState, backStack, appViewModel) }
}

private fun updateRootDestination(
    appViewModel: AppViewModel,
    rootDestination: RootDestinations,
    backStack: NavBackStack<NavKey>
) {
    val rootDestinations = RootDestinations.entries
    appViewModel.updateActiveDrawerItem(rootDestination.destination)
    for (i in backStack.indices.reversed()) {
        val shouldPop =
            rootDestinations.none { it.destination == backStack[i] }
        if (shouldPop) {
            backStack.removeAt(i)
        }
    }
    backStack.add(rootDestination.destination)
}