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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.initiative.InitiativeEntryEntity
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.common.composable.EmptyList
import com.feko.generictabletoprpg.common.composable.InputField
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
            LazyColumn(
                Modifier
                    .weight(1f)
                    .padding(8.dp),
                listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(entries, key = { it.id }) {
                    InitiativeListItem(
                        it,
                        onEditButtonClicked = { viewModel.showEditDialog(it) },
                        onUpdateKeepOnReset = { initiativeEntry, keepOnReset ->
                            viewModel.updateKeepOnReset(initiativeEntry, keepOnReset)
                        }
                    )
                }
            }
        }
        Box(Modifier.fillMaxWidth()) {
            Row(
                Modifier.align(Alignment.Center),
                Arrangement.spacedBy(8.dp)
            ) {
                FloatingActionButton(onClick = {
                    viewModel.showCreateNewDialog()
                }) {
                    Icon(Icons.Default.Add, "")
                }
                FloatingActionButton({}) {
                    Icon(Icons.Default.Refresh, "")
                }
                FloatingActionButton({}) {
                    Icon(painterResource(R.drawable.bolt), "")
                }
                FloatingActionButton({}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "")
                }
            }
        }
    }
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
fun InitiativeListItem(
    initiativeEntry: InitiativeEntryEntity,
    onEditButtonClicked: (InitiativeEntryEntity) -> Unit,
    onUpdateKeepOnReset: (InitiativeEntryEntity, Boolean) -> Unit
) {
    val isHighlighted = initiativeEntry.hasTurn
    val isLairAction = initiativeEntry.isLairAction
    Card(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground).takeIf { isHighlighted }
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
                        IconAndTextWithTooltip(
                            R.drawable.heart,
                            initiativeEntry.health.toString(),
                            stringResource(R.string.health)
                        )
                        IconAndTextWithTooltip(
                            R.drawable.shield,
                            initiativeEntry.armorClass.toString(),
                            stringResource(R.string.armor_class)
                        )
                        if (initiativeEntry.hasLegendaryActions) {
                            IconAndTextWithTooltip(
                                R.drawable.bolt,
                                initiativeEntry.legendaryActions.toString(),
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
                            onClick = {}
                        ) { Icon(Icons.Filled.Favorite, "") }
                        IconButton(
                            onClick = { onEditButtonClicked(initiativeEntry) }
                        ) { Icon(Icons.Filled.Edit, "") }
                    }
                    IconButton(
                        onClick = {}
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
        InitiativeEntryEntity(1, "Larry", 10, 18, 19, 3, 14, 7, false, false),
        onEditButtonClicked = {},
        onUpdateKeepOnReset = { _, _ -> }
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
    BasicAlertDialog(
        onDismissRequest = onDialogDismissed,
        properties = DialogProperties()
    ) {
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
                                legendaryActions = it.toIntOrNull() ?: 0
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
