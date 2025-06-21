package com.feko.generictabletoprpg.features.searchall.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.common.ui.RootDestinations
import com.feko.generictabletoprpg.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.common.ui.components.OverviewListItem
import com.feko.generictabletoprpg.common.ui.components.OverviewScreen
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.features.action.Action
import com.feko.generictabletoprpg.features.ammunition.Ammunition
import com.feko.generictabletoprpg.features.armor.Armor
import com.feko.generictabletoprpg.features.condition.Condition
import com.feko.generictabletoprpg.features.disease.Disease
import com.feko.generictabletoprpg.features.feat.Feat
import com.feko.generictabletoprpg.features.filters.asFilter
import com.feko.generictabletoprpg.features.filters.ui.Filter
import com.feko.generictabletoprpg.features.spell.Spell
import com.feko.generictabletoprpg.features.weapon.Weapon
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.ActionDetailsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AmmunitionDetailsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ArmorDetailsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ConditionDetailsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.DiseaseDetailsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.FeatDetailsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SpellDetailsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.WeaponDetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.Direction
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination<RootGraph>
@Composable
fun SearchAllScreen(
    defaultFilter: Int? = null,
    startedForResult: Boolean = false,
    resultNavigator: ResultBackNavigator<Long>,
    navigator: DestinationsNavigator,
    appViewModel: AppViewModel,
    onNavigationIconClick: () -> Unit
) {
    val refreshesPending by appViewModel.refreshesPending.collectAsState()
    val viewModel: SearchAllViewModel = koinViewModel { parametersOf(defaultFilter?.asFilter()) }
    appViewModel.updateActiveDrawerItem(RootDestinations.SearchAll)
    if (RootDestinations.SearchAll in refreshesPending) {
        viewModel.refreshItems()
        appViewModel.itemsRefreshed(RootDestinations.SearchAll)
    }
    Scaffold(
        topBar = {
            GttrpgTopAppBar(R.string.search_all_title.asText(), onNavigationIconClick) {
                val filterOffButtonVisible by viewModel.filter.offButtonVisible.collectAsState(false)
                if (filterOffButtonVisible) {
                    IconButton(onClick = { viewModel.filter.resetFilter() }) {
                        Icon(painterResource(R.drawable.filter_list_off), "")
                    }
                }
                val filterButtonVisible by viewModel.filter.isButtonVisible.collectAsState(false)
                if (filterButtonVisible) {
                    IconButton(onClick = { viewModel.filterRequested() }) {
                        Icon(painterResource(R.drawable.filter_list), "")
                    }
                }
            }
        }
    ) { paddingValues ->
        val isBottomSheetVisible = viewModel.isBottomSheetVisible.collectAsState(false)
        OverviewScreen(
            viewModel,
            listItem = { item, _, _ ->
                OverviewListItem(
                    item,
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (startedForResult) {
                                resultNavigator.navigateBack((item as IIdentifiable).id)
                            } else {
                                navigator.navigate(getNavRouteInternal(item))
                            }
                        })
            },
            modifier = Modifier.padding(paddingValues),
            uniqueListItemKey = { getUniqueListItemKey(it) },
            searchFieldHintResource = R.string.search_everywhere,
            isBottomSheetVisible = isBottomSheetVisible.value,
            onBottomSheetHidden = { viewModel.bottomSheetHidden() },
            bottomSheetContent = {
                val filter = viewModel.filter.activeFilter.collectAsState()
                Filter(
                    filter.value,
                    isTypeFixed = startedForResult
                ) { updatedFilter ->
                    viewModel.filter.filterUpdated(updatedFilter)
                }
            }
        )
    }
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