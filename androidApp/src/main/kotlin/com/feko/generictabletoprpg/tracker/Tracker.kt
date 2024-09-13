package com.feko.generictabletoprpg.tracker

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.ButtonState
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.composable.AddFABButtonWithDropdown
import com.feko.generictabletoprpg.common.composable.OverviewScreen
import com.feko.generictabletoprpg.common.composable.ToastMessage
import com.feko.generictabletoprpg.destinations.SearchAllScreenDestination
import com.feko.generictabletoprpg.searchall.getNavRouteInternal
import com.feko.generictabletoprpg.searchall.getUniqueListItemKey
import com.feko.generictabletoprpg.theme.Typography
import com.feko.generictabletoprpg.tracker.actions.AbilityActions
import com.feko.generictabletoprpg.tracker.actions.HealthActions
import com.feko.generictabletoprpg.tracker.actions.HitDiceActions
import com.feko.generictabletoprpg.tracker.actions.NumberActions
import com.feko.generictabletoprpg.tracker.actions.PercentageActions
import com.feko.generictabletoprpg.tracker.actions.SpellListActions
import com.feko.generictabletoprpg.tracker.actions.SpellSlotActions
import com.feko.generictabletoprpg.tracker.actions.TextListActions
import com.feko.generictabletoprpg.tracker.dialogs.AlertDialogComposable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parameterSetOf

@Destination
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
            ) {
                DropdownMenuContent(viewModel)
            }
        },
        isAlertDialogVisible = isAlertDialogVisible,
        alertDialogComposable = {
            AlertDialogComposable(
                viewModel,
                groupName,
                navigator
            )
        },
        isReorderable = true,
        onItemReordered = { from, to ->
            viewModel.itemReordered(from.index, to.index)
        },
        searchFieldHintResource = R.string.search_everywhere
    )
}

@Composable
fun OverviewListItem(
    item: Any,
    isDragged: Boolean,
    state: ReorderableLazyListState,
    navigator: DestinationsNavigator,
    viewModel: TrackerViewModel
) {
    if (item is TrackedThing) {
        TrackedThing(item, isDragged, state, navigator, viewModel)
    } else {
        com.feko.generictabletoprpg.common.composable.OverviewListItem(
            item,
            Modifier.clickable {
                navigator.navigate(getNavRouteInternal(item))
            })
    }
}

@Composable
private fun TrackedThing(
    item: TrackedThing,
    isDragged: Boolean,
    state: ReorderableLazyListState,
    navigator: DestinationsNavigator,
    viewModel: ITrackerViewModel
) {
    val targetElevation: Dp
    val targetScale: Float
    if (isDragged) {
        targetElevation = 16.dp
        targetScale = 1.05f
    } else {
        targetElevation = 0.dp
        targetScale = 1f
    }
    val elevation =
        animateDpAsState(targetElevation, label = "Tracked thing dragging elevation")
    val scale =
        animateFloatAsState(targetScale, label = "Tracked thing dragging scale")
    Card(
        onClick = {
            if (item is SpellList) {
                viewModel.showPreviewSpellListDialog(item)
            }
        },
        Modifier
            .fillMaxWidth()
            .scale(scale.value)
            .shadow(elevation.value)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
                    .detectReorder(state)
            ) {
                Icon(
                    Icons.Default.Menu,
                    "",
                    Modifier.align(Alignment.Center)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp)
            ) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) firstRow@{
                    Text(
                        item.name,
                        style = Typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    if (item.type == TrackedThing.Type.Text) {
                        return@firstRow
                    }
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .padding(vertical = 4.dp)
                            .background(Color.Gray)
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(85.dp)
                    ) {
                        Text(item.getPrintableValue())
                        if (item is Health) {
                            if (item.temporaryHp > 0) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painterResource(R.drawable.shield_with_heart),
                                        "",
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(item.temporaryHp.toString(), style = Typography.bodySmall)
                                }
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) row@{
                            if (item.type == TrackedThing.Type.Percentage) {
                                return@row
                            }
                            Text(item.type.name, style = Typography.bodySmall)
                            if (item is SpellSlot) {
                                Text("Lv ${item.level}", style = Typography.bodySmall)
                            }
                        }
                    }
                }
                var expanded by remember { mutableStateOf(false) }
                if (item.type == TrackedThing.Type.Text) {
                    Text(
                        item.getPrintableValue(),
                        Modifier.clickable {
                            expanded = !expanded
                        },
                        maxLines = if (expanded) Int.MAX_VALUE else 3,
                        style = Typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                when (item.type) {
                    TrackedThing.Type.Percentage -> PercentageActions(item, viewModel)
                    TrackedThing.Type.Number -> NumberActions(item, viewModel)
                    TrackedThing.Type.Health -> HealthActions(item, viewModel)
                    TrackedThing.Type.Ability -> AbilityActions(item, viewModel)
                    TrackedThing.Type.SpellSlot -> SpellSlotActions(item, viewModel)
                    TrackedThing.Type.SpellList -> SpellListActions(item, navigator, viewModel)
                    TrackedThing.Type.Text ->
                        TextListActions(item, expanded, viewModel) { expanded = it }

                    TrackedThing.Type.HitDice -> HitDiceActions(item, viewModel)
                    TrackedThing.Type.None -> Unit
                }
            }
        }
    }
}

@Preview
@Composable
private fun SpellListTrackedThingPreview() {
    TrackedThing(
        SpellList(0, "Spell List", "", 0, 0),
        false,
        rememberReorderableLazyListState(onMove = { _, _ -> }),
        EmptyDestinationsNavigator,
        EmptyTrackerViewModel
    )
}

