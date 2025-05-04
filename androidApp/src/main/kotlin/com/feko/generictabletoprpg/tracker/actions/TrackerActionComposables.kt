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
import com.feko.generictabletoprpg.tracker.Health
import com.feko.generictabletoprpg.tracker.ItemActionsBase
import com.feko.generictabletoprpg.tracker.Percentage
import com.feko.generictabletoprpg.tracker.Stats
import com.feko.generictabletoprpg.tracker.TrackedThing

@Composable
fun PercentageActions(
    item: Percentage,
    onAddButtonClicked: () -> Unit,
    onSubtractButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {
    ItemActionsBase(onEditButtonClicked, onDeleteButtonClicked) {
        IconButton(
            onClick = onAddButtonClicked,
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Add, "")
        }
        IconButton(
            onClick = onSubtractButtonClicked,
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
    }
}

@Composable
fun NumberActions(
    item: TrackedThing,
    onAddButtonClicked: () -> Unit,
    onSubtractButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {
    ItemActionsBase(onEditButtonClicked, onDeleteButtonClicked) {
        IconButton(
            onClick = onAddButtonClicked,
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Add, "")
        }
        IconButton(
            onClick = onSubtractButtonClicked,
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
    }
}

@Composable
fun HealthActions(
    health: Health,
    onHealButtonClicked: () -> Unit,
    onDamageButtonClicked: () -> Unit,
    onAddTemporaryHpButtonClicked: () -> Unit,
    onResetButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {
    ItemActionsBase(onEditButtonClicked, onDeleteButtonClicked) {
        IconButton(
            onClick = onHealButtonClicked,
            enabled = health.canAdd()
        ) {
            Icon(painterResource(R.drawable.heart_plus), "")
        }
        IconButton(
            onClick = onDamageButtonClicked,
            enabled = health.canSubtract()
        ) {
            Icon(painterResource(R.drawable.heart_minus), "")
        }
        IconButton(
            onClick = onAddTemporaryHpButtonClicked,
            enabled = true
        ) {
            Icon(painterResource(R.drawable.shield_with_heart), "")
        }
        IconButton(
            onClick = onResetButtonClicked,
            enabled = health.canAdd() || health.temporaryHp > 0
        ) {
            Icon(Icons.Default.Refresh, "")
        }
    }
}

@Composable
fun AbilityActions(
    canSubtract: Boolean,
    onSubtractClicked: () -> Unit,
    canRefresh: Boolean,
    onRefreshClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {
    ItemActionsBase(onEditButtonClicked, onDeleteButtonClicked) {
        IconButton(
            onClick = onSubtractClicked,
            enabled = canSubtract
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
        IconButton(
            onClick = onRefreshClicked,
            enabled = canRefresh
        ) {
            Icon(Icons.Default.Refresh, "")
        }
    }
}

@Composable
fun SpellSlotActions(
    canSubtract: Boolean,
    onSubtractClicked: () -> Unit,
    canRefresh: Boolean,
    onRefreshClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit
) {
    ItemActionsBase(onEditButtonClicked, onDeleteButtonClicked) {
        IconButton(
            onClick = onSubtractClicked,
            enabled = canSubtract
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
        IconButton(
            onClick = onRefreshClicked,
            enabled = canRefresh
        ) {
            Icon(Icons.Default.Refresh, "")
        }
    }
}

@Composable
fun SpellListActions(
    isListButtonEnabled: Boolean,
    onListButtonClicked: () -> Unit,
    onAddButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {
    ItemActionsBase(onEditButtonClicked, onDeleteButtonClicked) {
        IconButton(
            onClick = onListButtonClicked,
            enabled = isListButtonEnabled
        ) {
            Icon(Icons.AutoMirrored.Filled.List, "")
        }
        IconButton(onClick = onAddButtonClicked) {
            Icon(Icons.Default.Add, "")
        }
    }
}

@Composable
fun StatsActions(
    stats: Stats,
    onPreviewButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit
) {
    ItemActionsBase(
        onEditButtonClicked = onEditButtonClicked,
        onDeleteButtonClicked = onDeleteButtonClicked
    ) {
        IconButton(
            onClick = onPreviewButtonClicked,
            enabled = stats.serializedItem.stats.any()
        ) {
            Icon(Icons.AutoMirrored.Filled.List, "")
        }
    }
}

@Composable
fun TextActions(
    canTextBeExpanded: Boolean,
    expanded: Boolean,
    onExpandStateChanged: (Boolean) -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit
) {
    ItemActionsBase(onEditButtonClicked, onDeleteButtonClicked) {
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
    canSubtract: Boolean,
    onSubtractClicked: () -> Unit,
    canAdd: Boolean,
    onAddClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit
) {
    ItemActionsBase(onEditButtonClicked, onDeleteButtonClicked) {
        IconButton(
            onClick = onSubtractClicked,
            enabled = canSubtract,
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
        IconButton(
            onClick = onAddClicked,
            enabled = canAdd
        ) {
            Icon(Icons.Default.Add, "")
        }
    }
}
