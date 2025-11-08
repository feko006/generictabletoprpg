package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.heart_minus
import com.feko.generictabletoprpg.heart_plus
import com.feko.generictabletoprpg.shared.common.ui.components.addIcon
import com.feko.generictabletoprpg.shared.common.ui.components.keyboardArrowDownIcon
import com.feko.generictabletoprpg.shared.common.ui.components.keyboardArrowUpIcon
import com.feko.generictabletoprpg.shared.common.ui.components.listIcon
import com.feko.generictabletoprpg.shared.common.ui.components.refreshIcon
import com.feko.generictabletoprpg.shared.common.ui.components.removeIcon
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
            Icon(addIcon, "")
        }
        IconButton(
            onClick = onSubtractButtonClicked,
            enabled = item.canSubtract
        ) {
            Icon(removeIcon, "")
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
            Icon(addIcon, "")
        }
        IconButton(
            onClick = onSubtractButtonClicked,
            enabled = item.canSubtract
        ) {
            Icon(removeIcon, "")
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
            Icon(refreshIcon, "")
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
            Icon(removeIcon, "")
        }
        IconButton(
            onClick = onRefreshClicked,
            enabled = canRefresh
        ) {
            Icon(refreshIcon, "")
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
            Icon(removeIcon, "")
        }
        IconButton(
            onClick = onRefreshClicked,
            enabled = canRefresh
        ) {
            Icon(refreshIcon, "")
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
            Icon(listIcon, "")
        }
        IconButton(onClick = onAddButtonClicked) {
            Icon(addIcon, "")
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
            Icon(listIcon, "")
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
                Icon(keyboardArrowUpIcon, "")
            } else {
                Icon(keyboardArrowDownIcon, "")
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
            Icon(removeIcon, "")
        }
        IconButton(
            onClick = onAddClicked,
            enabled = canAdd
        ) {
            Icon(addIcon, "")
        }
    }
}