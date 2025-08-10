package com.feko.generictabletoprpg.features.encounter.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.features.encounter.InitiativeEntryEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ActionButtons(viewModel: EncounterViewModel) {
    val encounterState by viewModel.encounterState.collectAsState(EncounterState.Empty)
    Row(
        Modifier.fillMaxWidth(),
        Arrangement.spacedBy(LocalDimens.current.paddingSmall, Alignment.CenterHorizontally)
    ) {
        HorizontalFloatingToolbar(expanded = true) {
            if (encounterState.isAddButtonVisible) {
                IconButton(onClick = viewModel::showCreateNewDialog) {
                    Icon(Icons.Default.Add, "")
                }
            }
            if (encounterState.isResetButtonVisible) {
                IconButton(onClick = viewModel::showResetDialog) {
                    Icon(Icons.Default.Refresh, "")
                }
            }
        }
        HorizontalFloatingToolbar(expanded = true) {
            if (encounterState.isStartEncounterButtonVisible) {
                IconButton(onClick = viewModel::startInitiative) {
                    Icon(Icons.Default.PlayArrow, "")
                }
            }
            if (encounterState.isCompleteTurnButtonVisible) {
                IconButton(onClick = viewModel::concludeTurnOfCurrentEntry) {
                    Icon(Icons.Default.Check, "")
                }
            }
            if (encounterState.isLegendaryActionButtonVisible) {
                IconButton(onClick = viewModel::progressInitiativeWithLegendaryAction) {
                    Icon(painterResource(R.drawable.bolt), "")
                }
            }
            if (encounterState.isNextTurnButtonVisible) {
                IconButton(onClick = viewModel::progressInitiative) {
                    Icon(Icons.Default.SkipNext, "")
                }
            }
        }
    }
}

@Composable
fun InitiativeListItem(
    initiativeEntry: InitiativeEntryEntity,
    onUpdateInitiativeRequested: () -> Unit,
    onUpdateKeepOnReset: (Boolean) -> Unit,
    onHealButtonClicked: () -> Unit,
    onDuplicateButtonClicked: () -> Unit,
    onDamageButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isHighlighted = initiativeEntry.hasTurn
    val isLairAction = initiativeEntry.isLairAction
    val isTurnCompleted = initiativeEntry.isTurnCompleted
    val outlineColor =
        if (isTurnCompleted) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onBackground
    Card(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .then(modifier),
        MaterialTheme.shapes.extraLarge,
        border = BorderStroke(2.dp, outlineColor).takeIf { isHighlighted }
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .padding(horizontal = 8.dp),
                    Arrangement.spacedBy(8.dp),
                    Alignment.CenterVertically
                ) {
                    val canEditInitiative = !initiativeEntry.isLairAction
                    Text(
                        initiativeEntry.initiative.toString(),
                        Modifier.clickable(canEditInitiative) { onUpdateInitiativeRequested() },
                        color = if (canEditInitiative) MaterialTheme.colorScheme.primary else Color.Unspecified,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        if (isLairAction) stringResource(R.string.lair_action) else initiativeEntry.name,
                        Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                if (!isLairAction) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        Arrangement.SpaceAround
                    ) {
                        if (initiativeEntry.hasHealth) {
                            IconAndTextWithTooltip(
                                R.drawable.heart,
                                initiativeEntry.health.toString(),
                                stringResource(R.string.health)
                            )
                        }
                        if (initiativeEntry.hasArmorClass) {
                            IconAndTextWithTooltip(
                                R.drawable.shield,
                                initiativeEntry.armorClass.toString(),
                                stringResource(R.string.armor_class)
                            )
                        }
                        if (initiativeEntry.hasLegendaryActions) {
                            IconAndTextWithTooltip(
                                R.drawable.bolt,
                                initiativeEntry.printableLegendaryActions,
                                stringResource(R.string.legendary_actions)
                            )
                        }
                        if (initiativeEntry.hasSpellSaveDc) {
                            IconAndTextWithTooltip(
                                R.drawable.book_4_spark,
                                initiativeEntry.spellSaveDc.toString(),
                                stringResource(R.string.spell_save_dc)
                            )
                        }
                        if (initiativeEntry.hasSpellAttackModifier) {
                            IconAndTextWithTooltip(
                                R.drawable.wand_stars,
                                "+" + initiativeEntry.spellAttackModifier,
                                stringResource(R.string.spell_attack_modifier)
                            )
                        }
                    }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.Center
                ) {
                    val keepOnReset = initiativeEntry.keepOnRefresh
                    IconToggleButton(
                        checked = keepOnReset,
                        onCheckedChange = { onUpdateKeepOnReset(it) }) {
                        if (keepOnReset) {
                            Icon(Icons.Default.Star, "")
                        } else {
                            Icon(painterResource(R.drawable.star), "")
                        }
                    }
                    if (initiativeEntry.hasHealth) {
                        IconButton(onClick = onHealButtonClicked) {
                            Icon(painterResource(R.drawable.heart_plus), "")
                        }
                        IconButton(onClick = onDamageButtonClicked) {
                            Icon(painterResource(R.drawable.heart_minus), "")
                        }
                    }
                    if (!isLairAction) {
                        IconButton(onClick = onDuplicateButtonClicked) {
                            Icon(painterResource(R.drawable.content_copy), "")
                        }
                        IconButton(onClick = onEditButtonClicked) {
                            Icon(Icons.Filled.Edit, "")
                        }
                    }
                    IconButton(
                        onClick = onDeleteButtonClicked
                    ) { Icon(Icons.Filled.Delete, "") }
                }
            }
            if (isHighlighted) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.2f))
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun IconAndTextWithTooltip(@DrawableRes iconResource: Int, value: String, tooltipText: String) {
    val state = rememberBasicTooltipState(isPersistent = false)
    val coroutineScope = rememberCoroutineScope()
    BasicTooltipBox(
        TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = {
            ElevatedCard {
                Text(tooltipText, Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        },
        state = state,
        Modifier.clickable {
            if (state.isVisible) {
                state.dismiss()
            } else {
                coroutineScope.launch {
                    state.show()
                }
            }
        }) {
        IconAndText(iconResource, value)
    }
}

@Composable
fun IconAndText(@DrawableRes iconResource: Int, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(iconResource), "")
        Text(value)
    }
}

@Preview
@Composable
fun InitiativeListItemPreview() {
    InitiativeListItem(
        InitiativeEntryEntity(1, "Larry", 10, 18, 19, 3, 1, 14, 7, false, false, false),
        onUpdateInitiativeRequested = {},
        onUpdateKeepOnReset = {},
        onHealButtonClicked = {},
        onDamageButtonClicked = {},
        onDuplicateButtonClicked = {},
        onEditButtonClicked = {},
        onDeleteButtonClicked = {}
    )
}
