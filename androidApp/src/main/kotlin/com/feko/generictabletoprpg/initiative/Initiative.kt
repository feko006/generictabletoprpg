package com.feko.generictabletoprpg.initiative

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.initiative.InitiativeEntryEntity
import com.feko.generictabletoprpg.common.composable.ConfirmationDialog
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.common.composable.EmptyList
import com.feko.generictabletoprpg.common.composable.InputField
import com.feko.generictabletoprpg.common.composable.SelectFromListDialog
import com.feko.generictabletoprpg.common.composable.ToastMessage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun InitiativeScreen(appViewModel: AppViewModel) {
    appViewModel.set(
        appBarTitle = stringResource(R.string.initiative),
        navBarActions = listOf()
    )
    val viewModel: InitiativeViewModel = koinViewModel()
    val entries by viewModel.entries.collectAsState(listOf())
    Column {
        if (entries.isEmpty()) {
            Box(Modifier.weight(1f)) {
                EmptyList()
            }
        } else {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            LazyColumn(
                Modifier
                    .weight(1f)
                    .padding(8.dp),
                listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(entries, key = { _, item -> item.id }) { index, item ->
                    InitiativeListItem(
                        item,
                        onScrollToItem = {
                            coroutineScope.launch { listState.scrollToItem(index) }
                        },
                        onUpdateKeepOnReset = { initiativeEntry, keepOnReset ->
                            viewModel.updateKeepOnReset(initiativeEntry, keepOnReset)
                        },
                        onEditButtonClicked = viewModel::showEditDialog,
                        onDeleteButtonClicked = viewModel::showDeleteDialog
                    )
                }
            }
        }
        val currentRound by viewModel.currentRound.collectAsState(0)
        if (currentRound > 0) {
            Card(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            ) {
                Box(Modifier.padding(8.dp)) {
                    Text("%s: %d".format(stringResource(R.string.round), currentRound))
                }
            }
        }
        ActionButtons(viewModel, entries)
    }
    EditDialog(viewModel, entries)
    ConfirmDeletionDialog(viewModel)
    ConfirmResetDialog(viewModel)
    PickLegendaryActionDialog(viewModel)
    ToastMessage(viewModel.toastMessage)
}

@Composable
private fun ActionButtons(
    viewModel: InitiativeViewModel,
    entries: List<InitiativeEntryEntity>
) {
    Box(Modifier.fillMaxWidth()) {
        Row(
            Modifier.align(Alignment.Center),
            Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(onClick = viewModel::showCreateNewDialog) {
                Icon(Icons.Default.Add, "")
            }
            val isEntriesListEmpty by remember(entries) {
                derivedStateOf { entries.isEmpty() }
            }
            if (isEntriesListEmpty) return@Row
            FloatingActionButton(onClick = viewModel::showResetDialog) {
                Icon(Icons.Default.Refresh, "")
            }
            val isEncounterStarted by remember(entries) {
                derivedStateOf { entries.any { it.hasTurn } }
            }
            val isCurrentEntryTurnCompleted by remember(entries) {
                derivedStateOf { entries.firstOrNull { it.hasTurn }?.isTurnCompleted ?: false }
            }
            if (!isEncounterStarted) {
                FloatingActionButton(onClick = viewModel::startInitiative) {
                    Icon(Icons.Default.PlayArrow, "")
                }
            } else if (!isCurrentEntryTurnCompleted) {
                FloatingActionButton(onClick = viewModel::concludeTurnOfCurrentEntry) {
                    Icon(Icons.Default.Check, "")
                }
            } else {
                val anyEntryHasLegendaryActions by remember(entries) {
                    derivedStateOf { entries.any { it.hasLegendaryActions } }
                }
                if (anyEntryHasLegendaryActions) {
                    val isLegendaryActionsButtonEnabled by remember(entries) {
                        derivedStateOf { entries.any { it.canUseLegendaryAction } }
                    }
                    FloatingActionButton(
                        onClick = {
                            if (isLegendaryActionsButtonEnabled) {
                                viewModel.progressInitiativeWithLegendaryAction()
                            }
                        },
                        Modifier.alpha(if (isLegendaryActionsButtonEnabled) 1f else 0.4f)
                    ) {
                        Icon(painterResource(R.drawable.bolt), "")
                    }
                }
                FloatingActionButton(onClick = viewModel::progressInitiative) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "")
                }
            }
        }
    }
}

@Composable
private fun EditDialog(
    viewModel: InitiativeViewModel,
    entries: List<InitiativeEntryEntity>
) {
    val isEditAlertDialogVisible by viewModel.editAlertDialog.isVisible.collectAsState(false)
    if (isEditAlertDialogVisible) {
        val initiativeEntry by viewModel.editAlertDialog.editedItem.collectAsState(
            InitiativeEntryEntity.Empty
        )
        val isLairActionsButtonVisible by remember(entries) {
            derivedStateOf {
                entries.none { it.isLairAction }
            }
        }
        EditInitiativeEntryAlertDialog(
            initiativeEntry,
            isLairActionsButtonVisible,
            onItemUpdated = { viewModel.editAlertDialog.updateEditedItem(it) },
            onEditFinished = { viewModel.createOrUpdateInitiativeEntry(it) },
            onAddLairActions = { viewModel.addLairActions() },
            onDialogDismissed = { viewModel.editAlertDialog.dismiss() }
        )
    }
}

@Composable
private fun ConfirmDeletionDialog(viewModel: InitiativeViewModel) {
    val isConfirmDeletionDialogVisible
            by viewModel.confirmDeletionDialog.isVisible.collectAsState(false)
    if (isConfirmDeletionDialogVisible) {
        ConfirmationDialog(
            onConfirm = { viewModel.deleteEntry(viewModel.confirmDeletionDialog.editedItem.value) },
            onDialogDismissed = { viewModel.confirmDeletionDialog.dismiss() }
        )
    }
}

@Composable
private fun ConfirmResetDialog(viewModel: InitiativeViewModel) {
    val isConfirmResetDialogVisible by viewModel.confirmResetDialog.isVisible.collectAsState(false)
    if (isConfirmResetDialogVisible) {
        ConfirmationDialog(
            onConfirm = { viewModel.resetInitiative() },
            onDialogDismissed = { viewModel.confirmResetDialog.dismiss() },
            dialogTitle = R.string.reset_dialog_title,
            dialogMessage = R.string.reset_encounter_message
        )
    }
}

@Composable
private fun PickLegendaryActionDialog(viewModel: InitiativeViewModel) {
    val isPickLegendaryActionDialogVisible
            by viewModel.pickLegendaryActionDialog.isVisible.collectAsState(false)
    if (isPickLegendaryActionDialogVisible) {
        val entriesWithLegendaryActions
                by viewModel.pickLegendaryActionDialog.editedItem.collectAsState()
        SelectFromListDialog(
            R.string.select_legendary_action,
            entriesWithLegendaryActions,
            getListItemKey = { it.id },
            onItemSelected = { viewModel.useLegendaryActionAndProgressInitiative(it) },
            onDialogDismissed = { viewModel.pickLegendaryActionDialog.dismiss() }
        ) {
            ListItem(
                headlineContent = { Text(it.name) },
                trailingContent = { Text(it.printableLegendaryActions) }
            )
        }
    }
}

@Composable
fun InitiativeListItem(
    initiativeEntry: InitiativeEntryEntity,
    onScrollToItem: () -> Unit,
    onUpdateKeepOnReset: (InitiativeEntryEntity, Boolean) -> Unit,
    onEditButtonClicked: (InitiativeEntryEntity) -> Unit,
    onDeleteButtonClicked: (InitiativeEntryEntity) -> Unit
) {
    val isHighlighted = initiativeEntry.hasTurn
    val isLairAction = initiativeEntry.isLairAction
    val isTurnCompleted = initiativeEntry.isTurnCompleted
    val outlineColor =
        if (isTurnCompleted) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onBackground
    var previousHighlightedState by remember { mutableStateOf(isHighlighted) }
    var previousTurnCompletedState by remember { mutableStateOf(isTurnCompleted) }
    LaunchedEffect(isHighlighted, isTurnCompleted) {
        if (
            !previousHighlightedState && isHighlighted
            || !previousTurnCompletedState && isTurnCompleted
        ) {
            onScrollToItem()
        }
        previousHighlightedState = isHighlighted
        previousTurnCompletedState = isTurnCompleted
    }
    Card(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
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
                    Text(
                        initiativeEntry.initiative.toString(),
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
                        onCheckedChange = {
                            onUpdateKeepOnReset(initiativeEntry, it)
                        }) {
                        if (keepOnReset) {
                            Icon(Icons.Default.Star, "")
                        } else {
                            Icon(painterResource(R.drawable.star), "")
                        }
                    }
                    if (!isLairAction) {
                        IconButton(
                            onClick = { onEditButtonClicked(initiativeEntry) }
                        ) { Icon(Icons.Filled.Edit, "") }
                    }
                    IconButton(
                        onClick = { onDeleteButtonClicked(initiativeEntry) }
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
        TooltipDefaults.rememberPlainTooltipPositionProvider(),
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
        onScrollToItem = {},
        onUpdateKeepOnReset = { _, _ -> },
        onEditButtonClicked = {},
        onDeleteButtonClicked = {}
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditInitiativeEntryAlertDialog(
    initiativeEntry: InitiativeEntryEntity,
    isLairActionsButtonVisible: Boolean,
    onItemUpdated: (InitiativeEntryEntity) -> Unit,
    onEditFinished: (InitiativeEntryEntity) -> Unit,
    onAddLairActions: () -> Unit,
    onDialogDismissed: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDialogDismissed) {
        Card {
            Column(Modifier.padding(16.dp), Arrangement.spacedBy(16.dp)) {
                val dialogTitle by remember(initiativeEntry) {
                    derivedStateOf {
                        if (initiativeEntry.isSavedInDatabase) R.string.edit
                        else R.string.add
                    }
                }
                DialogTitle(dialogTitle)

                val name = initiativeEntry.name
                InputField(
                    value = name,
                    label = stringResource(R.string.name),
                    onValueChange = {
                        onItemUpdated(initiativeEntry.copy(name = it))
                    },
                    isInputFieldValid = { initiativeEntry.isNameValid },
                    autoFocus = !initiativeEntry.isSavedInDatabase
                )

                val initiative = initiativeEntry.initiative
                InputField(
                    value = initiative.toString(),
                    label = stringResource(R.string.initiative),
                    onValueChange = {
                        onItemUpdated(initiativeEntry.copy(initiative = it.toIntOrNull() ?: 0))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                val health = initiativeEntry.health
                InputField(
                    value = health.toString(),
                    label = stringResource(R.string.health),
                    onValueChange = {
                        onItemUpdated(initiativeEntry.copy(health = it.toIntOrNull() ?: 0))
                    },
                    isInputFieldValid = { initiativeEntry.isHealthValid },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                val armorClass = initiativeEntry.armorClass
                InputField(
                    value = armorClass.toString(),
                    label = stringResource(R.string.armor_class),
                    onValueChange = {
                        onItemUpdated(initiativeEntry.copy(armorClass = it.toIntOrNull() ?: 0))
                    },
                    isInputFieldValid = { initiativeEntry.isArmorClassValid },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                val legendaryActions = initiativeEntry.legendaryActions
                InputField(
                    value = legendaryActions.toString(),
                    label = stringResource(R.string.legendary_actions),
                    onValueChange = {
                        onItemUpdated(
                            initiativeEntry.copy(
                                legendaryActions = it.toIntOrNull() ?: 0,
                                availableLegendaryActions = it.toIntOrNull() ?: 0
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                val spellSaveDc = initiativeEntry.spellSaveDc
                InputField(
                    value = spellSaveDc.toString(),
                    label = stringResource(R.string.spell_save_dc),
                    onValueChange = {
                        onItemUpdated(initiativeEntry.copy(spellSaveDc = it.toIntOrNull() ?: 0))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                val spellAttackModifier = initiativeEntry.spellAttackModifier
                InputField(
                    value = spellAttackModifier.toString(),
                    label = stringResource(R.string.spell_attack_modifier),
                    onValueChange = {
                        onItemUpdated(
                            initiativeEntry.copy(spellAttackModifier = it.toIntOrNull() ?: 0)
                        )
                    },
                    onFormSubmit = {
                        if (initiativeEntry.isEntryValid) {
                            onEditFinished(initiativeEntry)
                            onDialogDismissed()
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

                if (isLairActionsButtonVisible) {
                    TextButton(
                        onClick = {
                            onAddLairActions()
                            onDialogDismissed()
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .wrapContentWidth()
                    ) { Text(stringResource(R.string.add_lair_actions)) }
                }
                TextButton(
                    onClick = {
                        onEditFinished(initiativeEntry)
                        onDialogDismissed()
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .wrapContentWidth(),
                    enabled = initiativeEntry.isEntryValid
                ) { Text(stringResource(R.string.confirm)) }
            }
        }
    }
}
