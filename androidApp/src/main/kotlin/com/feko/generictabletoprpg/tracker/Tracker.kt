package com.feko.generictabletoprpg.tracker

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.ButtonState
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.composable.AddFABButtonWithDropdown
import com.feko.generictabletoprpg.common.composable.OverviewListItem
import com.feko.generictabletoprpg.common.composable.OverviewScreen
import com.feko.generictabletoprpg.common.composable.ToastMessage
import com.feko.generictabletoprpg.searchall.getNavRouteInternal
import com.feko.generictabletoprpg.searchall.getUniqueListItemKey
import com.feko.generictabletoprpg.tracker.dialogs.TrackerAlertDialog
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.SearchAllScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parameterSetOf

@Destination<RootGraph>
@Composable
fun TrackerScreen(
    groupId: Long,
    groupName: String,
    appViewModel: AppViewModel,
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<SearchAllScreenDestination, Long>
) {
    val viewModel: TrackerViewModel =
        koinViewModel(parameters = { parameterSetOf(groupId, groupName) })
    appViewModel
        .set(
            appBarTitle = stringResource(R.string.tracker_title),
            navBarActions = listOf(
                ButtonState(Icons.Default.Refresh) { viewModel.refreshAllRequested() }
            )
        )
    resultRecipient.onNavResult { result ->
        if (result is NavResult.Value<Long>) {
            viewModel.addSpellToList(result.value)
        }
    }
    ToastMessage(viewModel.toast)
    OverviewScreen(
        viewModel = viewModel,
        listItem = { item, isDragged, state ->
            TrackerListItem(item, isDragged, state!!, navigator, viewModel)
        },
        uniqueListItemKey = {
            getUniqueListItemKey(it)
        },
        fabButton = { modifier ->
            val expanded by viewModel.fabDropdown.isMenuExpanded.collectAsState(false)
            AddFABButtonWithDropdown(
                expanded = expanded,
                modifier = modifier,
                onDismissRequest = { viewModel.fabDropdown.dismiss() },
                onFabClicked = { viewModel.fabDropdown.toggleFabDropdownRequested() }
            ) { DropdownMenuContent { type, context -> viewModel.showCreateDialog(type, context) } }
        },
        isReorderable = true,
        onItemReordered = { from, to ->
            viewModel.itemReordered(from.index, to.index)
        },
        searchFieldHintResource = R.string.search_everywhere
    )
    TrackerAlertDialog(viewModel, navigator)
}

@Composable
fun TrackerListItem(
    item: Any,
    isDragged: Boolean,
    reorderableLazyListState: ReorderableLazyListState,
    navigator: DestinationsNavigator,
    viewModel: TrackerViewModel
) {
    if (item is TrackedThing) {
        when (item) {
            is Ability -> AbilityListItem(isDragged, item, reorderableLazyListState, viewModel)

            is Health -> HealthListItem(isDragged, item, reorderableLazyListState, viewModel)

            is HitDice -> HitDiceListItem(isDragged, item, reorderableLazyListState, viewModel)

            is Number -> NumberListItem(isDragged, item, reorderableLazyListState, viewModel)

            is Percentage ->
                PercentageListItem(isDragged, item, reorderableLazyListState, viewModel)

            is SpellList ->
                SpellListItem(isDragged, item, reorderableLazyListState, navigator, viewModel)

            is SpellSlot -> SpellSlotListItem(isDragged, item, reorderableLazyListState, viewModel)

            is Stats -> StatsListItem(isDragged, item, reorderableLazyListState, viewModel)

            is Text -> TextListItem(isDragged, item, reorderableLazyListState, viewModel)

            TrackedThing.Companion.Empty,
            is GenericTrackedThing<*>,
            is JsonTrackedThing<*> -> Unit
        }
    } else {
        OverviewListItem(item, Modifier.clickable { navigator.navigate(getNavRouteInternal(item)) })
    }
}