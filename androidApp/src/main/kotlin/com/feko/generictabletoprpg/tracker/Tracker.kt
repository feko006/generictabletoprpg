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
import com.feko.generictabletoprpg.common.composable.OverviewScreen
import com.feko.generictabletoprpg.common.composable.ToastMessage
import com.feko.generictabletoprpg.searchall.getNavRouteInternal
import com.feko.generictabletoprpg.searchall.getUniqueListItemKey
import com.feko.generictabletoprpg.tracker.dialogs.AlertDialogComposable
import com.feko.generictabletoprpg.tracker.dialogs.TrackerAlertDialogs
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
    val isAlertDialogVisible by viewModel.alertDialog.isVisible.collectAsState(false)
    resultRecipient.onNavResult { result ->
        if (result is NavResult.Value<Long>) {
            viewModel.addSpellToList(result.value)
        }
    }
    ToastMessage(viewModel.toast)
    OverviewScreen(
        viewModel = viewModel,
        listItem = { item, isDragged, state ->
            OverviewListItem(item, isDragged, state!!, navigator, viewModel)
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
        isAlertDialogVisible = isAlertDialogVisible,
        alertDialogComposable = {
            AlertDialogComposable(viewModel, groupName, navigator)
        },
        isReorderable = true,
        onItemReordered = { from, to ->
            viewModel.itemReordered(from.index, to.index)
        },
        searchFieldHintResource = R.string.search_everywhere
    )
    TrackerAlertDialogs(viewModel)
}

@Composable
fun OverviewListItem(
    item: Any,
    isDragged: Boolean,
    reorderableLazyListState: ReorderableLazyListState,
    navigator: DestinationsNavigator,
    viewModel: TrackerViewModel
) {
    if (item is TrackedThing) {
        when (item) {
            is Ability -> TrackedThingListItem(isDragged) {
                AbilityListItemContent(item, reorderableLazyListState, viewModel)
            }

            is Health -> TrackedThingListItem(isDragged) {
                HealthListItemContent(item, reorderableLazyListState, viewModel)
            }

            is HitDice -> TrackedThingListItem(isDragged) {
                HitDiceListItemContent(item, reorderableLazyListState, viewModel)
            }

            is Number -> TrackedThingListItem(isDragged) {
                NumberListItemContent(item, reorderableLazyListState, viewModel)
            }

            is Percentage -> TrackedThingListItem(isDragged) {
                PercentageListItemContent(item, reorderableLazyListState, viewModel)
            }

            is SpellList -> TrackedThingListItem(
                isDragged,
                onItemClicked = {
                    viewModel.showPreviewSpellListDialog(item, resetListState = true)
                }) {
                SpellListItemContent(item, reorderableLazyListState, navigator, viewModel)
            }

            is SpellSlot -> TrackedThingListItem(isDragged) {
                SpellSlotListItemContent(item, reorderableLazyListState, viewModel)
            }

            is Stats -> TrackedThingListItem(
                isDragged,
                onItemClicked = { viewModel.showStatsDialog(item) }) {
                StatsListItemContent(item, reorderableLazyListState, viewModel)
            }

            is Text -> TrackedThingListItem(isDragged) {
                TextListItemContent(item, reorderableLazyListState, viewModel)
            }

            else -> {}
        }
    } else {
        com.feko.generictabletoprpg.common.composable.OverviewListItem(
            item,
            Modifier.clickable {
                navigator.navigate(getNavRouteInternal(item))
            })
    }
}