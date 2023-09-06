package com.feko.generictabletoprpg

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.feko.generictabletoprpg.action.ActionDetails
import com.feko.generictabletoprpg.ammunition.AmmunitionDetails
import com.feko.generictabletoprpg.armor.ArmorDetails
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackerGroupsScreen
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.TrackerScreen
import com.feko.generictabletoprpg.condition.ConditionDetails
import com.feko.generictabletoprpg.disease.DiseaseDetails
import com.feko.generictabletoprpg.feat.FeatDetails
import com.feko.generictabletoprpg.import.Import
import com.feko.generictabletoprpg.searchall.SearchAllScreen
import com.feko.generictabletoprpg.spell.SpellDetails
import com.feko.generictabletoprpg.weapon.WeaponDetails
import kotlinx.coroutines.launch

object Navigation {
    @Deprecated("To be removed with compose destinations")
    abstract class Destination(
        var screenTitle: String,
        var route: String,
        var isRootDestination: Boolean
    ) {
        abstract fun navHostComposable(
            navGraphBuilder: NavGraphBuilder,
            navController: NavHostController,
            appViewModel: AppViewModel
        )
    }

    @Deprecated("To be removed with compose destinations")
    interface DetailsNavRouteProvider {
        fun getNavRoute(id: Long): String
    }

    @Deprecated("To be removed with compose destinations")
    private val destinations: List<Destination> =
        listOf(
            SpellDetails,
            WeaponDetails,
            ArmorDetails,
            ActionDetails,
            ConditionDetails,
            DiseaseDetails,
            FeatDetails,
            AmmunitionDetails,
            Import
        )

    @Composable
    private fun Host(
        navController: NavHostController,
        appViewModel: AppViewModel
    ) {
        NavHost(
            navController = navController,
            startDestination = "searchall"
        ) {
            composable(route = "searchall", arguments = listOf()) {
                SearchAllScreen(navController, appViewModel)
            }
            composable(route = "trackergroups", arguments = listOf()) {
                TrackerGroupsScreen(
                    navController,
                    appViewModel,
                    getTrackerNavRoute = { "tracker/$it" })
            }
            composable(
                route = "tracker/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.LongType
                    })
            ) {
                val groupId = it.arguments!!.getLong("id")
                TrackerScreen(groupId, navController, appViewModel)
            }
            destinations.forEach {
                it.navHostComposable(this, navController, appViewModel)
            }
        }
    }

    @Composable
    fun Drawer(
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
                        Pair("Search All", "searchall"),
                        Pair("Tracker", "trackergroups"),
                        Pair("Import", "import")
                    )
                    drawerItemData.forEach { (screenTitle, route) ->
                        NavigationDrawerItem(
                            label = { Text(screenTitle) },
                            selected = activeDrawerItem.value == route,
                            onClick = {
                                activeDrawerItem.value = route
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate(route) {
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
            Host(navController, appViewModel)
        }
    }
}