package com.feko.generictabletoprpg.features.tracker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.heart_minus
import com.feko.generictabletoprpg.heart_plus
import com.feko.generictabletoprpg.shared.features.tracker.model.StatsContainer
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import com.feko.generictabletoprpg.shared.features.tracker.model.canAdd
import com.feko.generictabletoprpg.shared.features.tracker.model.canSubtract
import com.feko.generictabletoprpg.shield_with_heart
import org.jetbrains.compose.resources.painterResource

@Composable
fun PercentageActions(
    item: TrackedThing,
    onAddButtonClicked: () -> Unit,
    onSubtractButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {
    ItemActionsBase(onEditButtonClicked, onDeleteButtonClicked) {
        IconButton(
            onClick = onAddButtonClicked,
            enabled = item.canAdd
        ) {
            Icon(Icons.Default.Add, "")
        }
        IconButton(
            onClick = onSubtractButtonClicked,
            enabled = item.canSubtract
        ) {
            Icon(Icons.Default.Remove, "")
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
            enabled = item.canAdd
        ) {
            Icon(Icons.Default.Add, "")
        }
        IconButton(
            onClick = onSubtractButtonClicked,
            enabled = item.canSubtract
        ) {
            Icon(Icons.Default.Remove, "")
        }
    }
}

@Composable
fun HealthActions(
    health: TrackedThing,
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
            enabled = health.canAdd
        ) {
            Icon(painterResource(Res.drawable.heart_plus), "")
        }
        IconButton(
            onClick = onDamageButtonClicked,
            enabled = health.canSubtract
        ) {
            Icon(painterResource(Res.drawable.heart_minus), "")
        }
        IconButton(
            onClick = onAddTemporaryHpButtonClicked,
            enabled = true
        ) {
            Icon(painterResource(Res.drawable.shield_with_heart), "")
        }
        IconButton(
            onClick = onResetButtonClicked,
            enabled = health.canAdd || health.temporaryHp > 0
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
            Icon(Icons.Default.Remove, "")
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
            Icon(Icons.Default.Remove, "")
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
    stats: TrackedThing,
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
            enabled = (stats.serializedItem as StatsContainer).stats.any()
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
            Icon(Icons.Default.Remove, "")
        }
        IconButton(
            onClick = onAddClicked,
            enabled = canAdd
        ) {
            Icon(Icons.Default.Add, "")
        }
    }
}
