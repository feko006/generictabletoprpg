package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.IText
import com.feko.generictabletoprpg.common.composable.AlertDialogBase
import com.feko.generictabletoprpg.common.composable.BoxWithScrollIndicator
import com.feko.generictabletoprpg.common.composable.ConfirmationDialog
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.common.composable.SelectFromListDialog
import com.feko.generictabletoprpg.searchall.getUniqueListItemKey
import com.feko.generictabletoprpg.spell.Spell
import com.feko.generictabletoprpg.spell.SpellRange
import com.feko.generictabletoprpg.theme.Typography
import com.feko.generictabletoprpg.tracker.SpellList
import com.feko.generictabletoprpg.tracker.SpellListEntry
import com.feko.generictabletoprpg.tracker.TrackerViewModel
import com.feko.generictabletoprpg.tracker.cantripSpellsCount
import com.feko.generictabletoprpg.tracker.dialogs.ITrackerDialog
import com.feko.generictabletoprpg.tracker.filterPreparedAndCantrips
import com.feko.generictabletoprpg.tracker.preparedSpellsCount
import com.ramcosta.composedestinations.generated.destinations.SimpleSpellDetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch

sealed interface ISpellListDialogDialogs {
    data object None : ISpellListDialogDialogs

    data class SelectSpellSlotDialog(
        val availableSlots: List<Int>,
        val onDismiss: () -> Unit,
        val title: IText = IText.StringResourceText(R.string.select_slot_level_for_casting_spell)
    ) : ISpellListDialogDialogs

    data class ConfirmSpellRemovalDialog(
        val spellListEntry: SpellListEntry,
        val onDismiss: () -> Unit,
        val title: IText = IText.StringResourceText(R.string.confirm_spell_removal_from_list_dialog_title)
    ) : ISpellListDialogDialogs
}

@Composable
fun SpellListDialogWithViewModel(
    dialog: ITrackerDialog.SpellListDialog,
    viewModel: TrackerViewModel,
    navigator: DestinationsNavigator
) {
    SpellListDialog(
        dialog.spellList,
        dialog.isFilteringByPreparedSpells,
        navigator,
        dialog.title.text(),
        viewModel.spellListState,
        onDialogDismissed = dialog.onDismiss,
        onFilteringByPreparedStateChanged = { viewModel.setShowingPreparedSpells(it) },
        canSpellBeCast = { level -> viewModel.canCastSpell(level) },
        onSpellPreparedStateChanged = { spellListEntry, isPrepared ->
            viewModel.changeSpellListEntryPreparedState(
                dialog.spellList,
                spellListEntry,
                isPrepared
            )
        },
        onCastSpellRequested = { level -> viewModel.castSpellRequested(level) },
        onRemoveSpellRequested = { viewModel.removeSpellFromSpellListRequested(it) }
    )
    when (dialog.secondaryDialog) {
        is ISpellListDialogDialogs.SelectSpellSlotDialog ->
            SelectSpellSlotLevelToCastDialog(
                dialog.secondaryDialog,
                dialog.onDismiss
            ) { viewModel.castSpell(it) }

        is ISpellListDialogDialogs.ConfirmSpellRemovalDialog ->
            ConfirmSpellRemovalFromListDialog(
                dialog.secondaryDialog,
                dialog.onDismiss
            ) {
                viewModel.removeSpellFromSpellList(
                    dialog.spellList,
                    dialog.secondaryDialog.spellListEntry
                )
            }

        is ISpellListDialogDialogs.None -> {}
    }
}

@Composable
private fun SpellListDialog(
    spellList: SpellList,
    isFilteringByPrepared: Boolean,
    navigator: DestinationsNavigator,
    dialogTitle: String,
    spellListState: LazyListState,
    onDialogDismissed: () -> Unit,
    onFilteringByPreparedStateChanged: (Boolean) -> Unit,
    canSpellBeCast: (level: Int) -> Boolean,
    onSpellPreparedStateChanged: (SpellListEntry, Boolean) -> Unit,
    onCastSpellRequested: (level: Int) -> Unit,
    onRemoveSpellRequested: (spell: SpellListEntry) -> Unit
) {
    AlertDialogBase(
        onDialogDismissed,
        dialogTitle = { DialogTitle(dialogTitle) },
        dialogButtons = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                Arrangement.SpaceBetween
            ) {
                val coroutineScope = rememberCoroutineScope()
                Row {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                spellListState.animateScrollToItem(0)
                            }
                        },
                        enabled = spellListState.canScrollBackward
                    ) { Icon(painterResource(R.drawable.vertical_align_top), "") }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                spellListState.animateScrollToItem(spellList.serializedItem.size - 1)
                            }
                        },
                        enabled = spellListState.canScrollForward
                    ) { Icon(painterResource(R.drawable.vertical_align_bottom), "") }
                }
                TextButton(
                    onClick = onDialogDismissed,
                    modifier = Modifier.wrapContentWidth()
                ) { Text(stringResource(R.string.dismiss)) }
            }
        }
    ) {
        val numberOfPreparedSpells = spellList.serializedItem.preparedSpellsCount()
        val numberOfCantripSpells = spellList.serializedItem.cantripSpellsCount()
        Row(
            Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                buildString {
                    append(stringResource(R.string.known))
                    append(": ")
                    append(spellList.serializedItem.size)
                    if (numberOfPreparedSpells > 0) {
                        append(", ")
                        append(stringResource(R.string.prepared))
                        append(": ")
                        append(numberOfPreparedSpells)
                    }
                    if (numberOfCantripSpells > 0) {
                        append(", ")
                        append(stringResource(R.string.cantrips))
                        append(": ")
                        append(numberOfCantripSpells)
                    }
                },
                Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = Typography.bodySmall
            )
            if (numberOfPreparedSpells > 0 || numberOfCantripSpells > 0) {
                ElevatedFilterChip(
                    isFilteringByPrepared,
                    onClick = {
                        onFilteringByPreparedStateChanged(!isFilteringByPrepared)
                    },
                    label = {
                        Text(stringResource(R.string.prepared))
                    },
                    leadingIcon =
                        {
                            if (isFilteringByPrepared) {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(R.drawable.check_box_outline_blank),
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        }
                )
            } else {
                onFilteringByPreparedStateChanged(false)
            }
        }
        BoxWithScrollIndicator(
            spellListState,
            CardDefaults.cardColors().containerColor,
            Modifier.heightIn(0.dp, (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
        ) {
            LazyColumn(
                state = spellListState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    spellList.serializedItem.filterPreparedAndCantrips(isFilteringByPrepared),
                    key = { getUniqueListItemKey(it.toSpell()) }) { spellListEntry ->
                    SpellListEntryListItem(
                        spellListEntry,
                        canSpellBeCast(spellListEntry.level),
                        navigator,
                        onSpellPreparedStateChanged = {
                            onSpellPreparedStateChanged(spellListEntry, it)
                        },
                        onCastSpellClicked = { onCastSpellRequested(spellListEntry.level) },
                        onRemoveSpellRequested = { onRemoveSpellRequested(spellListEntry) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SpellListEntryListItem(
    spellListEntry: SpellListEntry,
    canCastSpell: Boolean,
    navigator: DestinationsNavigator,
    onSpellPreparedStateChanged: (Boolean) -> Unit,
    onCastSpellClicked: () -> Unit,
    onRemoveSpellRequested: () -> Unit,
) {
    ElevatedCard(onClick = {
        navigator.navigate(SimpleSpellDetailsScreenDestination.invoke(spellListEntry.toSpell()))
    }) {
        Column {
            Row(
                Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    spellListEntry.name,
                    Modifier
                        .weight(1f)
                        .padding(vertical = 16.dp),
                    style = Typography.titleMedium
                )
                if (spellListEntry.level > 0) {
                    Column(
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                            Checkbox(
                                spellListEntry.isPrepared,
                                onCheckedChange = onSpellPreparedStateChanged
                            )
                        }
                        Text(
                            stringResource(R.string.prepared),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            style = Typography.labelSmall
                        )
                    }
                }
            }
            HorizontalDivider(Modifier.padding(horizontal = 2.dp))
            Text(
                "${stringResource(R.string.level)} ${spellListEntry.level}, ${spellListEntry.school}",
                Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = Typography.bodySmall
            )
            Text(
                "${stringResource(R.string.casting_time)}: ${spellListEntry.castingTime}",
                Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = Typography.bodySmall
            )
            HorizontalDivider(Modifier.padding(horizontal = 2.dp))
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceAround,
                Alignment.CenterVertically
            ) {
                if (spellListEntry.level != 0) {
                    TextButton(
                        onClick = onCastSpellClicked,
                        enabled = canCastSpell
                    ) {
                        Text(stringResource(R.string.cast))
                    }
                }
                TextButton(onClick = onRemoveSpellRequested) {
                    Text(stringResource(R.string.remove))
                }
            }
        }
    }
}

@Composable
fun SelectSpellSlotLevelToCastDialog(
    dialog: ISpellListDialogDialogs.SelectSpellSlotDialog,
    onDialogDismissed: () -> Unit,
    onItemSelected: (Int) -> Unit
) {
    SelectFromListDialog(
        dialog.title.text(),
        dialog.availableSlots,
        getListItemKey = { it },
        onItemSelected,
        onDialogDismissed
    ) { ListItem(headlineContent = { Text("${stringResource(R.string.level)} $it") }) }
}

@Composable
private fun ConfirmSpellRemovalFromListDialog(
    dialog: ISpellListDialogDialogs.ConfirmSpellRemovalDialog,
    onDialogDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    ConfirmationDialog(onConfirm, onDialogDismiss, dialog.title.text())
}

@Preview
@Composable
fun SpellListDialogPreview() {
    SpellListDialog(
        spellList = SpellList(0, "Spell List", "", 0, 0)
            .apply {
                serializedItem = mutableListOf(
                    getSpellListEntry1(),
                    getSpellListEntry2()
                )
            },
        isFilteringByPrepared = false,
        navigator = EmptyDestinationsNavigator,
        dialogTitle = "Spell List",
        spellListState = rememberLazyListState(),
        onDialogDismissed = {},
        onFilteringByPreparedStateChanged = {},
        canSpellBeCast = { true },
        onSpellPreparedStateChanged = { _, _ -> },
        onCastSpellRequested = {},
        onRemoveSpellRequested = {}
    )
}

@Preview
@Composable
fun SpellListEntryListItemPreview() {
    SpellListEntryListItem(
        getSpellListEntry2(),
        canCastSpell = true,
        EmptyDestinationsNavigator,
        onSpellPreparedStateChanged = {},
        onCastSpellClicked = {},
        onRemoveSpellRequested = {},
    )
}

private fun getSpellListEntry1() = SpellListEntry(
    id = 1,
    name = "Spell 1",
    description = "",
    school = "Abjuration",
    duration = "",
    concentration = true,
    level = 0,
    source = "",
    components = Spell.SpellComponents(
        verbal = true,
        somatic = true,
        material = true,
        materialComponent = null
    ),
    castingTime = "1 action",
    classesThatCanCast = listOf(),
    range = SpellRange(
        isSelf = true,
        isTouch = true,
        isSight = true,
        distance = 0,
        unit = null
    ),
    isRitual = true,
    isPrepared = false
)

private fun getSpellListEntry2() = SpellListEntry(
    id = 2,
    name = "Spell 2",
    description = "",
    school = "Conjuration",
    duration = "",
    concentration = true,
    level = 1,
    source = "",
    components = Spell.SpellComponents(
        true,
        somatic = true,
        material = true,
        materialComponent = null
    ),
    castingTime = "1 bonus action",
    classesThatCanCast = listOf(),
    range = SpellRange(
        isSelf = true,
        isTouch = true,
        isSight = true,
        distance = 0,
        unit = null
    ),
    isRitual = true,
    isPrepared = true
)

