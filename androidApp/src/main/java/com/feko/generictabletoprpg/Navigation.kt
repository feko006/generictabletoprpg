package com.feko.generictabletoprpg

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.feko.generictabletoprpg.action.ActionDetails
import com.feko.generictabletoprpg.ammunition.AmmunitionDetails
import com.feko.generictabletoprpg.armor.ArmorDetails
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.Tracker
import com.feko.generictabletoprpg.condition.ConditionDetails
import com.feko.generictabletoprpg.disease.DiseaseDetails
import com.feko.generictabletoprpg.feat.FeatDetails
import com.feko.generictabletoprpg.import.Import
import com.feko.generictabletoprpg.searchall.SearchAll
import com.feko.generictabletoprpg.spell.SpellDetails
import com.feko.generictabletoprpg.weapon.WeaponDetails
import kotlinx.coroutines.launch

object Navigation {
    interface Destination {
        val screenTitle: String
        val route: String
        val isRootDestination: Boolean
        fun navHostComposable(
            navGraphBuilder: NavGraphBuilder,
            navController: NavHostController,
            appBarTitle: MutableState<String>
        )
    }

    interface DetailsNavRouteProvider {
        fun getNavRoute(id: Long): String
    }

    private val destinations: List<Destination> =
        listOf(
            SearchAll,
            Tracker,
            SpellDetails,
//            SpellOverview,
            WeaponDetails,
//            WeaponOverview,
            ArmorDetails,
//            ArmorOverview,
//            ActionOverview,
            ActionDetails,
//            ConditionOverview,
            ConditionDetails,
//            DiseaseOverview,
            DiseaseDetails,
            FeatDetails,
//            FeatOverview,
            AmmunitionDetails,
//            AmmunitionOverview,
//            SpellSlots,
            Import
        )

    private val rootDestinations: List<Destination> =
        destinations.filter { it.isRootDestination }

    private val firstDestination: Destination =
        destinations.first { it is SearchAll }

    @Composable
    private fun Host(
        navController: NavHostController,
        appBarTitle: MutableState<String>
    ) {
        NavHost(
            navController = navController,
            startDestination = firstDestination.route
        ) {
            destinations.forEach {
                it.navHostComposable(this, navController, appBarTitle)
            }
        }
    }

    @Composable
    fun Drawer(
        drawerState: DrawerState,
        paddingValues: PaddingValues,
        navController: NavHostController,
        appBarTitle: MutableState<String>
    ) {
        val scope = rememberCoroutineScope()
        val activeDrawerItem = rememberSaveable { mutableStateOf(firstDestination.route) }
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    rootDestinations.forEach { destination ->
                        val route = destination.route
                        NavigationDrawerItem(
                            label = { Text(destination.screenTitle) },
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
            Host(navController, appBarTitle)
        }
    }
}