package com.feko.generictabletoprpg.tracker.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.filters.SpellFilter
import com.feko.generictabletoprpg.filters.index
import com.feko.generictabletoprpg.tracker.Health
import com.feko.generictabletoprpg.tracker.IntTrackedThing
import com.feko.generictabletoprpg.tracker.ItemActionsBase
import com.feko.generictabletoprpg.tracker.SpellList
import com.feko.generictabletoprpg.tracker.Stats
import com.feko.generictabletoprpg.tracker.TrackedThing
import com.ramcosta.composedestinations.generated.destinations.SearchAllScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun PercentageActions(
    item: TrackedThing,
    viewModel: IPercentageActionsTrackerViewModel
) {
    ItemActionsBase(item, viewModel) {
        IconButton(
            onClick = { viewModel.addToPercentageRequested(item) },
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Add, "")
        }
        IconButton(
            onClick = { viewModel.subtractFromPercentageRequested(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
    }
}

@Composable
fun NumberActions(
    item: TrackedThing,
    viewModel: INumberActionsTrackerViewModel
) {
    ItemActionsBase(item, viewModel) {
        IconButton(
            onClick = { viewModel.addToNumberRequested(item) },
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Add, "")
        }
        IconButton(
            onClick = { viewModel.subtractFromNumberRequested(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
    }
}

@Composable
fun HealthActions(
    item: TrackedThing,
    viewModel: IHealthActionsTrackerViewModel
) {
    ItemActionsBase(item, viewModel) {
        IconButton(
            onClick = { viewModel.healRequested(item) },
            enabled = item.canAdd()
        ) {
            Icon(painterResource(R.drawable.heart_plus), "")
        }
        IconButton(
            onClick = { viewModel.takeDamageRequested(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.heart_minus), "")
        }
        IconButton(
            onClick = { viewModel.addTemporaryHp(item) },
            enabled = true
        ) {
            Icon(painterResource(R.drawable.shield_with_heart), "")
        }
        IconButton(
            onClick = { viewModel.resetValueToDefault(item) },
            enabled = item.canAdd() || (item as Health).temporaryHp > 0
        ) {
            Icon(Icons.Default.Refresh, "")
        }
    }
}

@Composable
fun AbilityActions(
    item: TrackedThing,
    viewModel: IAbilityActionsTrackerViewModel
) {
    ItemActionsBase(item, viewModel) {
        IconButton(
            onClick = { viewModel.useAbility(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
        IconButton(
            onClick = { viewModel.resetValueToDefault(item) },
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Refresh, "")
        }
    }
}

@Composable
fun SpellSlotActions(
    item: TrackedThing,
    viewModel: ISpellSlotActionsTrackerViewModel
) {
    ItemActionsBase(item, viewModel) {
        IconButton(
            onClick = { viewModel.useSpell(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
        IconButton(
            onClick = { viewModel.resetValueToDefault(item) },
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Refresh, "")
        }
    }
}

@Composable
fun SpellListActions(
    item: TrackedThing,
    navigator: DestinationsNavigator,
    viewModel: ISpellListActionsTrackerViewModel
) {
    ItemActionsBase(item, viewModel) {
        val spellList = item as SpellList
        IconButton(
            onClick = { viewModel.showPreviewSpellListDialog(spellList, resetListState = true) },
            enabled = spellList.serializedItem.any()
        ) {
            Icon(Icons.AutoMirrored.Filled.List, "")
        }
        IconButton(
            onClick = {
                viewModel.addingSpellToList(spellList)
                navigator.navigate(SearchAllScreenDestination(SpellFilter().index(), true))
            },
            enabled = true
        ) {
            Icon(Icons.Default.Add, "")
        }
    }
}

@Composable
fun StatsActions(item: TrackedThing, viewModel: IStatsActionsTrackerViewModel) {
    ItemActionsBase(item, viewModel) {
        val stats = item as Stats
        IconButton(
            onClick = { viewModel.showStatsDialog(stats) },
            enabled = stats.serializedItem.stats.any()
        ) {
            Icon(Icons.AutoMirrored.Filled.List, "")
        }
    }
}

@Composable
fun TextActions(
    item: TrackedThing,
    canTextBeExpanded: Boolean,
    expanded: Boolean,
    viewModel: IBasicActionsTrackerViewModel,
    onExpandStateChanged: (Boolean) -> Unit
) {
    ItemActionsBase(item, viewModel) {
        if (!canTextBeExpanded) return@ItemActionsBase
        IconButton(
            onClick = {
                onExpandStateChanged(!expanded)
            },
            enabled = true
        ) {
            if (expanded) {
                Icon(Icons.Default.KeyboardArrowUp, "")
            } else {
                Icon(Icons.Default.KeyboardArrowDown, "")
            }
        }
    }
}

@Composable
fun HitDiceActions(
    item: TrackedThing,
    viewModel: IHitDiceActionsTrackerViewModel
) {
    if (item !is IntTrackedThing) return
    ItemActionsBase(item, viewModel) {
        IconButton(
            onClick = { viewModel.useHitDie(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
        IconButton(
            onClick = { viewModel.restoreHitDie(item) },
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Add, "")
        }
    }
}
