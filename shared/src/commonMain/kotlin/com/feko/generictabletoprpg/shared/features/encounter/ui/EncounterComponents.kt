package com.feko.generictabletoprpg.shared.features.encounter.ui

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.armor_class
import com.feko.generictabletoprpg.book_4_spark
import com.feko.generictabletoprpg.health
import com.feko.generictabletoprpg.heart_minus
import com.feko.generictabletoprpg.heart_plus
import com.feko.generictabletoprpg.lair_action
import com.feko.generictabletoprpg.legendary_actions
import com.feko.generictabletoprpg.shared.common.ui.components.addIcon
import com.feko.generictabletoprpg.shared.common.ui.components.boltIcon
import com.feko.generictabletoprpg.shared.common.ui.components.checkIcon
import com.feko.generictabletoprpg.shared.common.ui.components.contentCopyIcon
import com.feko.generictabletoprpg.shared.common.ui.components.deleteIcon
import com.feko.generictabletoprpg.shared.common.ui.components.editIcon
import com.feko.generictabletoprpg.shared.common.ui.components.favoriteBorderIcon
import com.feko.generictabletoprpg.shared.common.ui.components.playArrowIcon
import com.feko.generictabletoprpg.shared.common.ui.components.refreshIcon
import com.feko.generictabletoprpg.shared.common.ui.components.shieldIcon
import com.feko.generictabletoprpg.shared.common.ui.components.skipNextIcon
import com.feko.generictabletoprpg.shared.common.ui.components.starIcon
import com.feko.generictabletoprpg.shared.common.ui.components.starOutlineIcon
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.features.encounter.InitiativeEntryEntity
import com.feko.generictabletoprpg.spell_attack_modifier
import com.feko.generictabletoprpg.spell_save_dc
import com.feko.generictabletoprpg.wand_stars
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

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
                    Icon(addIcon, "")
                }
            }
            if (encounterState.isResetButtonVisible) {
                IconButton(onClick = viewModel::showResetDialog) {
                    Icon(refreshIcon, "")
                }
            }
        }
        HorizontalFloatingToolbar(expanded = true) {
            if (encounterState.isStartEncounterButtonVisible) {
                IconButton(onClick = viewModel::startInitiative) {
                    Icon(playArrowIcon, "")
                }
            }
            if (encounterState.isCompleteTurnButtonVisible) {
                IconButton(onClick = viewModel::concludeTurnOfCurrentEntry) {
                    Icon(checkIcon, "")
                }
            }
            if (encounterState.isLegendaryActionButtonVisible) {
                IconButton(onClick = viewModel::progressInitiativeWithLegendaryAction) {
                    Icon(boltIcon, "")
                }
            }
            if (encounterState.isNextTurnButtonVisible) {
                IconButton(onClick = viewModel::progressInitiative) {
                    Icon(skipNextIcon, "")
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
                        if (isLairAction) stringResource(Res.string.lair_action) else initiativeEntry.name,
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
                                favoriteBorderIcon,
                                initiativeEntry.health.toString(),
                                stringResource(Res.string.health)
                            )
                        }
                        if (initiativeEntry.hasArmorClass) {
                            IconAndTextWithTooltip(
                                shieldIcon,
                                initiativeEntry.armorClass.toString(),
                                stringResource(Res.string.armor_class)
                            )
                        }
                        if (initiativeEntry.hasLegendaryActions) {
                            IconAndTextWithTooltip(
                                boltIcon,
                                initiativeEntry.printableLegendaryActions,
                                stringResource(Res.string.legendary_actions)
                            )
                        }
                        if (initiativeEntry.hasSpellSaveDc) {
                            IconAndTextWithTooltip(
                                vectorResource(Res.drawable.book_4_spark),
                                initiativeEntry.spellSaveDc.toString(),
                                stringResource(Res.string.spell_save_dc)
                            )
                        }
                        if (initiativeEntry.hasSpellAttackModifier) {
                            IconAndTextWithTooltip(
                                vectorResource(Res.drawable.wand_stars),
                                "+" + initiativeEntry.spellAttackModifier,
                                stringResource(Res.string.spell_attack_modifier)
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
                            Icon(starIcon, "")
                        } else {
                            Icon(starOutlineIcon, "")
                        }
                    }
                    if (initiativeEntry.hasHealth) {
                        IconButton(onClick = onHealButtonClicked) {
                            Icon(painterResource(Res.drawable.heart_plus), "")
                        }
                        IconButton(onClick = onDamageButtonClicked) {
                            Icon(painterResource(Res.drawable.heart_minus), "")
                        }
                    }
                    if (!isLairAction) {
                        IconButton(onClick = onDuplicateButtonClicked) {
                            Icon(contentCopyIcon, "")
                        }
                        IconButton(onClick = onEditButtonClicked) {
                            Icon(editIcon, "")
                        }
                    }
                    IconButton(
                        onClick = onDeleteButtonClicked
                    ) { Icon(deleteIcon, "") }
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
fun IconAndTextWithTooltip(imageVector: ImageVector, value: String, tooltipText: String) {
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
        IconAndText(imageVector, value)
    }
}

@Composable
fun IconAndText(imageVector: ImageVector, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector, "")
        Text(value)
    }
}