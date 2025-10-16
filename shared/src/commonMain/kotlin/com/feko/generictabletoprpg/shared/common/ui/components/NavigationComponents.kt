package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.compose.material3.DrawerState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
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
import com.feko.generictabletoprpg.shared.features.tracker.ui.TrackerGroupsScreen
import com.feko.generictabletoprpg.shared.features.tracker.ui.TrackerScreen
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import com.feko.generictabletoprpg.shared.features.weapon.ui.WeaponDetailsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    NavDisplay(
        backStack,
        entryDecorators = listOf(
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategy = ListDetailSceneStrategy(
            BackNavigationBehavior.Companion.PopLatest,
            PaneScaffoldDirective.Companion.Default.copy(
                maxHorizontalPartitions = panes,
                maxVerticalPartitions = panes
            )
        )
    ) { key ->
        if (key !is INavigationDestination) {
            throw IllegalStateException("Destination target unknown.")
        }
        when (key) {
            INavigationDestination.TrackerGroupsDestination ->
                NavEntry(key) {
                    TrackerGroupsScreen(appViewModel, onNavigationIconClick) { id, name ->
                        backStack.add(INavigationDestination.TrackerDestination(id, name))
                    }
                }

            is INavigationDestination.TrackerDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.listPane()
                ) {
                    TrackerScreen(
                        key.id,
                        key.name,
                        onNavigationIconClick,
                        searchAllResultViewModel,
                        onNavigateToSimpleSpellDetailsScreen = {
                            backStack.add(
                                INavigationDestination.SimpleSpellDetailsDestination(it)
                            )
                        },
                        onOpenDetails = {
                            backStack.add(getDetailsDestination(it))
                        },
                        onSelectSpellRequest = {
                            backStack.add(
                                INavigationDestination.SearchAllDestination(
                                    SpellFilter().index(),
                                    isShownForResult = true
                                )
                            )
                        }
                    )
                }

            INavigationDestination.EncounterDestination ->
                NavEntry(key) { EncounterScreen(onNavigationIconClick) }

            is INavigationDestination.SearchAllDestination ->
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

            is INavigationDestination.ActionDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { ActionDetailsScreen(key.id, onNavigationIconClick) }

            is INavigationDestination.AmmunitionDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { AmmunitionDetailsScreen(key.id, onNavigationIconClick) }

            is INavigationDestination.ArmorDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { ArmorDetailsScreen(key.id, onNavigationIconClick) }

            is INavigationDestination.ConditionDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { ConditionDetailsScreen(key.id, onNavigationIconClick) }

            is INavigationDestination.DiseaseDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { DiseaseDetailsScreen(key.id, onNavigationIconClick) }

            is INavigationDestination.FeatDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { FeatDetailsScreen(key.id, onNavigationIconClick) }

            is INavigationDestination.SpellDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { SpellDetailsScreen(key.id, onNavigationIconClick) }

            is INavigationDestination.WeaponDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { WeaponDetailsScreen(key.id, onNavigationIconClick) }

            is INavigationDestination.SimpleSpellDetailsDestination ->
                NavEntry(
                    key,
                    metadata = ListDetailSceneStrategy.extraPane()
                ) {
                    SimpleSpellDetailsScreen(key.spell, onNavigationIconClick)
                }

            INavigationDestination.ImportDestination ->
                NavEntry(key) { ImportScreen(appViewModel, onNavigationIconClick) }
        }
    }
}

fun getDetailsDestination(item: Any): INavigationDestination {
    val id = (item as IIdentifiable).id
    return when (item) {
        is Action -> INavigationDestination.ActionDetailsDestination(id)
        is Ammunition -> INavigationDestination.AmmunitionDetailsDestination(id)
        is Armor -> INavigationDestination.ArmorDetailsDestination(id)
        is Condition -> INavigationDestination.ConditionDetailsDestination(id)
        is Disease -> INavigationDestination.DiseaseDetailsDestination(id)
        is Feat -> INavigationDestination.FeatDetailsDestination(id)
        is Spell -> INavigationDestination.SpellDetailsDestination(id)
        is Weapon -> INavigationDestination.WeaponDetailsDestination(id)
        else -> throw IllegalStateException("Unknown list item")
    }
}