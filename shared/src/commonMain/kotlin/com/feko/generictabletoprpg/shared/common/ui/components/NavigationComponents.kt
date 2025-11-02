package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.ActionDetailsDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.AmmunitionDetailsDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.ArmorDetailsDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.ConditionDetailsDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.DiseaseDetailsDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.EncounterDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.FeatDetailsDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.ImportDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.SearchAllDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.SimpleSpellDetailsDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.SpellDetailsDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.SpellListDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.TrackerDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.TrackerGroupsDestination
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination.WeaponDetailsDestination
import com.feko.generictabletoprpg.shared.common.ui.components.navigation3.ListDetailSceneStrategy
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.ResultViewModel
import com.feko.generictabletoprpg.shared.features.action.Action
import com.feko.generictabletoprpg.shared.features.action.ui.ActionDetailsScreen
import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition
import com.feko.generictabletoprpg.shared.features.ammunition.ui.AmmunitionDetailsScreen
import com.feko.generictabletoprpg.shared.features.armor.Armor
import com.feko.generictabletoprpg.shared.features.armor.ui.ArmorDetailsScreen
import com.feko.generictabletoprpg.shared.features.condition.Condition
import com.feko.generictabletoprpg.shared.features.condition.ui.ConditionDetailsScreen
import com.feko.generictabletoprpg.shared.features.disease.Disease
import com.feko.generictabletoprpg.shared.features.disease.ui.DiseaseDetailsScreen
import com.feko.generictabletoprpg.shared.features.encounter.ui.EncounterScreen
import com.feko.generictabletoprpg.shared.features.feat.Feat
import com.feko.generictabletoprpg.shared.features.feat.ui.FeatDetailsScreen
import com.feko.generictabletoprpg.shared.features.filter.SpellFilter
import com.feko.generictabletoprpg.shared.features.filter.index
import com.feko.generictabletoprpg.shared.features.io.ui.ImportScreen
import com.feko.generictabletoprpg.shared.features.searchall.ui.SearchAllScreen
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.spell.ui.SimpleSpellDetailsScreen
import com.feko.generictabletoprpg.shared.features.spell.ui.SpellDetailsScreen
import com.feko.generictabletoprpg.shared.features.tracker.ui.SpellListScreen
import com.feko.generictabletoprpg.shared.features.tracker.ui.TrackerGroupsScreen
import com.feko.generictabletoprpg.shared.features.tracker.ui.TrackerScreen
import com.feko.generictabletoprpg.shared.features.tracker.ui.TrackerViewModel
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import com.feko.generictabletoprpg.shared.features.weapon.ui.WeaponDetailsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parameterSetOf

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun NavigationHost(
    scope: CoroutineScope,
    drawerState: DrawerState,
    backStack: NavBackStack<NavKey>,
    appViewModel: AppViewModel
) {
    val onNavigationIconClick: () -> Unit = {
        scope.launch {
            drawerState.apply {
                if (isOpen) close() else open()
            }
        }
    }

    val onNavigateBack: () -> Unit = {
        backStack.removeLastOrNull()
    }

    val searchAllResultViewModel = viewModel<ResultViewModel<Long>>()
    val panes =
        currentWindowAdaptiveInfo().windowSizeClass.let {
            when {
                it.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> 2
                it.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> 3
                else -> 1
            }
        }
    var trackerViewModel: TrackerViewModel? by remember { mutableStateOf(null) }
    NavDisplay(
        backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategy = ListDetailSceneStrategy(
            BackNavigationBehavior.Companion.PopUntilCurrentDestinationChange,
            PaneScaffoldDirective.Companion.Default.copy(
                maxHorizontalPartitions = panes,
                maxVerticalPartitions = panes
            ),
            ListDetailPaneScaffoldDefaults.adaptStrategies()
        )
    ) { key ->
        if (key !is INavigationDestination) {
            throw IllegalStateException("Destination target unknown.")
        }
        when (key) {
            TrackerGroupsDestination ->
                NavEntry(key) {
                    TrackerGroupsScreen(appViewModel, onNavigationIconClick) { id, name ->
                        backStack.add(TrackerDestination(id, name))
                    }
                }

            is TrackerDestination ->
                NavEntry(key, metadata = ListDetailSceneStrategy.listPane()) {
                    trackerViewModel =
                        koinViewModel(parameters = { parameterSetOf(key.id, key.name) })
                    TrackerScreen(
                        trackerViewModel!!,
                        onNavigationIconClick,
                        searchAllResultViewModel,
                        onNavigateToSpellListScreen = {
                            backStack.add(SpellListDestination)
                        },
                        onNavigateToSimpleSpellDetailsScreen = {
                            backStack.add(SimpleSpellDetailsDestination(it))
                        },
                        onOpenDetails = {
                            backStack.add(getDetailsDestination(it))
                        },
                        onSelectSpellRequest = {
                            backStack.add(
                                SearchAllDestination(SpellFilter().index(), isShownForResult = true)
                            )
                        }
                    )
                }

            EncounterDestination ->
                NavEntry(key) { EncounterScreen(onNavigationIconClick) }

            is SearchAllDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.listPane()
                ) {
                    SearchAllScreen(
                        appViewModel,
                        onNavigationIconClick,
                        onNavigateBack,
                        onOpenDetails = {
                            backStack.add(getDetailsDestination(it))
                        },
                        key.filterIndex,
                        searchAllResultViewModel.takeIf { key.isShownForResult }
                    )
                }

            is ActionDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { ActionDetailsScreen(key.id, onNavigationIconClick) }

            is AmmunitionDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { AmmunitionDetailsScreen(key.id, onNavigationIconClick) }

            is ArmorDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { ArmorDetailsScreen(key.id, onNavigationIconClick) }

            is ConditionDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { ConditionDetailsScreen(key.id, onNavigationIconClick) }

            is DiseaseDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { DiseaseDetailsScreen(key.id, onNavigationIconClick) }

            is FeatDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { FeatDetailsScreen(key.id, onNavigationIconClick) }

            is SpellDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { SpellDetailsScreen(key.id, onNavigationIconClick) }

            is WeaponDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { WeaponDetailsScreen(key.id, onNavigationIconClick) }

            is SimpleSpellDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.extraPane()
                ) {
                    SimpleSpellDetailsScreen(key.spell, onNavigationIconClick)
                }

            ImportDestination ->
                NavEntry(key) { ImportScreen(appViewModel, onNavigationIconClick) }

            is SpellListDestination ->
                NavEntry(key, metadata = ListDetailSceneStrategy.detailPane()) {
                    SpellListScreen(
                        trackerViewModel,
                        onNavigateToSimpleSpellDetailsScreen = {
                            backStack.add(SimpleSpellDetailsDestination(it))
                        },
                        onPopSpellListScreen = {
                            backStack.popUpTo<SpellListDestination>(inclusive = true)
                        }
                    )
                }
        }
    }
}

private inline fun <reified T : NavKey> NavBackStack<NavKey>.popUpTo(inclusive: Boolean) {
    if (any { it is T }) {
        for (i in indices.reversed()) {
            val navKey = this[i]
            if (navKey is T) {
                if (inclusive) removeAt(i)
                break
            }
            removeAt(i)
        }
    }
}

fun getDetailsDestination(item: Any): INavigationDestination {
    val id = (item as IIdentifiable).id
    return when (item) {
        is Action -> ActionDetailsDestination(id)
        is Ammunition -> AmmunitionDetailsDestination(id)
        is Armor -> ArmorDetailsDestination(id)
        is Condition -> ConditionDetailsDestination(id)
        is Disease -> DiseaseDetailsDestination(id)
        is Feat -> FeatDetailsDestination(id)
        is Spell -> SpellDetailsDestination(id)
        is Weapon -> WeaponDetailsDestination(id)
        else -> throw IllegalStateException("Unknown list item")
    }
}