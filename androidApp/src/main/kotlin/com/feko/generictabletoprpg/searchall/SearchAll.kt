package com.feko.generictabletoprpg.searchall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.ButtonState
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.RootDestinations
import com.feko.generictabletoprpg.action.Action
import com.feko.generictabletoprpg.ammunition.Ammunition
import com.feko.generictabletoprpg.armor.Armor
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.filter.filterUpdateRequested
import com.feko.generictabletoprpg.common.IIdentifiable
import com.feko.generictabletoprpg.common.composable.OverviewListItem
import com.feko.generictabletoprpg.common.composable.OverviewScreen
import com.feko.generictabletoprpg.condition.Condition
import com.feko.generictabletoprpg.destinations.ActionDetailsScreenDestination
import com.feko.generictabletoprpg.destinations.AmmunitionDetailsScreenDestination
import com.feko.generictabletoprpg.destinations.ArmorDetailsScreenDestination
import com.feko.generictabletoprpg.destinations.ConditionDetailsScreenDestination
import com.feko.generictabletoprpg.destinations.DiseaseDetailsScreenDestination
import com.feko.generictabletoprpg.destinations.FeatDetailsScreenDestination
import com.feko.generictabletoprpg.destinations.SpellDetailsScreenDestination
import com.feko.generictabletoprpg.destinations.WeaponDetailsScreenDestination
import com.feko.generictabletoprpg.disease.Disease
import com.feko.generictabletoprpg.feat.Feat
import com.feko.generictabletoprpg.filters.Filter
import com.feko.generictabletoprpg.spell.Spell
import com.feko.generictabletoprpg.weapon.Weapon
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun SearchAllScreen(
    navigator: DestinationsNavigator,
    appViewModel: AppViewModel
) {
    val refreshesPending by appViewModel.refreshesPending.collectAsState()
    val viewModel: SearchAllViewModel = koinViewModel()
    val navBarActions = mutableListOf<ButtonState>()
    val filterOffButtonVisible by viewModel.filterOffButtonVisible.collectAsState(false)
    if (filterOffButtonVisible) {
        navBarActions.add(
            ButtonState(painter = painterResource(R.drawable.filter_list_off)) {
                viewModel.filterUpdateRequested(null)
            }
        )
    }
    val filterButtonVisible by viewModel.filterButtonVisible.collectAsState(false)
    if (filterButtonVisible) {
        navBarActions.add(
            ButtonState(painter = painterResource(R.drawable.filter_list)) {
                viewModel.filterRequested()
            }
        )
    }
    appViewModel.run {
        set(
            appBarTitle = stringResource(R.string.search_all_title),
            navBarActions = navBarActions
        )
        updateActiveDrawerItem(RootDestinations.SearchAll)
    }
    if (RootDestinations.SearchAll in refreshesPending) {
        viewModel.refreshItems()
        appViewModel.itemsRefreshed(RootDestinations.SearchAll)
    }
    val isBottomSheetVisible = viewModel.isBottomSheetVisible.collectAsState(false)
    OverviewScreen(
        viewModel,
        listItem = { item, _, _ ->
            OverviewListItem(item) {
                navigator.navigate(getNavRouteInternal(item))
            }
        },
        uniqueListItemKey = { getUniqueListItemKey(it) },
        searchFieldHintResource = R.string.search_everywhere,
        isBottomSheetVisible = isBottomSheetVisible.value,
        onBottomSheetHidden = { viewModel.bottomSheetHidden() },
        bottomSheetContent = {
            val filter = viewModel.filter.collectAsState()
            Filter(filter.value) { updatedFilter ->
                viewModel.filterUpdateRequested(updatedFilter)
            }
        }
    )
}

fun getUniqueListItemKey(it: Any) = "${it::class}${(it as IIdentifiable).id}"

fun getNavRouteInternal(item: Any): Direction {
    val id = (item as IIdentifiable).id
    return when (item) {
        is Action -> ActionDetailsScreenDestination(id)
        is Ammunition -> AmmunitionDetailsScreenDestination(id)
        is Armor -> ArmorDetailsScreenDestination(id)
        is Condition -> ConditionDetailsScreenDestination(id)
        is Disease -> DiseaseDetailsScreenDestination(id)
        is Feat -> FeatDetailsScreenDestination(id)
        is Spell -> SpellDetailsScreenDestination(id)
        is Weapon -> WeaponDetailsScreenDestination(id)
        else -> throw IllegalStateException("Unknown list item")
    }
}