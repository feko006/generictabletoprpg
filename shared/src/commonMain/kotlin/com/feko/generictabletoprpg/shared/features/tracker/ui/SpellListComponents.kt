package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.cantrips
import com.feko.generictabletoprpg.known
import com.feko.generictabletoprpg.level
import com.feko.generictabletoprpg.prepared
import com.feko.generictabletoprpg.shared.common.ui.components.BoxWithScrollIndicator
import com.feko.generictabletoprpg.shared.common.ui.components.ConfirmationDialog
import com.feko.generictabletoprpg.shared.common.ui.components.SelectFromListDialog
import com.feko.generictabletoprpg.shared.common.ui.components.checkBoxOutlineBlankIcon
import com.feko.generictabletoprpg.shared.common.ui.components.doneIcon
import com.feko.generictabletoprpg.shared.common.ui.theme.Typography
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.tracker.model.SpellListEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.cantripSpellsCount
import com.feko.generictabletoprpg.shared.features.tracker.model.filterPreparedAndCantrips
import com.feko.generictabletoprpg.shared.features.tracker.model.preparedSpellsCount
import org.jetbrains.compose.resources.stringResource

@Composable
fun SpellListContent(
    dialog: ITrackerDialog.SpellListDialog,
    spellListState: LazyListState,
    onFilteringByPreparedStateChanged: (Boolean) -> Unit,
    canSpellBeCast: (Int) -> Boolean,
    onSpellPreparedStateChanged: (SpellListEntry, Boolean) -> Unit,
    onCastSpellRequested: (Int) -> Unit,
    onRemoveSpellRequested: (SpellListEntry) -> Unit,
    onSpellClick: (Spell) -> Unit,
    listModifier: Modifier = Modifier
) {
    @Suppress("UNCHECKED_CAST")
    val entries = dialog.spellList.serializedItem as List<SpellListEntry>
    val numberOfPreparedSpells = entries.preparedSpellsCount()
    val numberOfCantripSpells = entries.cantripSpellsCount()
    Row(
        Modifier.padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            buildString {
                append(stringResource(Res.string.known))
                append(": ")
                append(entries.size)
                if (numberOfPreparedSpells > 0) {
                    append(", ")
                    append(stringResource(Res.string.prepared))
                    append(": ")
                    append(numberOfPreparedSpells)
                }
                if (numberOfCantripSpells > 0) {
                    append(", ")
                    append(stringResource(Res.string.cantrips))
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
                dialog.isFilteringByPreparedSpells,
                onClick = {
                    onFilteringByPreparedStateChanged(!dialog.isFilteringByPreparedSpells)
                },
                label = {
                    Text(stringResource(Res.string.prepared))
                },
                leadingIcon =
                    {
                        if (dialog.isFilteringByPreparedSpells) {
                            Icon(
                                imageVector = doneIcon,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        } else {
                            Icon(
                                imageVector = checkBoxOutlineBlankIcon,
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
        listModifier
    ) {
        LazyColumn(
            state = spellListState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                entries.filterPreparedAndCantrips(dialog.isFilteringByPreparedSpells),
                key = { "${it.name}${it.id}" }) { spellListEntry ->
                SpellListEntryListItem(
                    spellListEntry,
                    canSpellBeCast(spellListEntry.level),
                    onSpellPreparedStateChanged = {
                        onSpellPreparedStateChanged(spellListEntry, it)
                    },
                    onCastSpellClicked = { onCastSpellRequested(spellListEntry.level) },
                    onRemoveSpellRequested = { onRemoveSpellRequested(spellListEntry) },
                    onSpellClick = onSpellClick
                )
            }
        }
    }
}

@Composable
fun SpellListSecondaryDialog(
    dialog: ITrackerDialog.SpellListDialog,
    viewModel: TrackerViewModel
) {
    when (dialog.secondaryDialog) {
        is ISpellListDialogDialogs.SelectSpellSlotDialog ->
            SelectSpellSlotLevelToCastDialog(
                dialog.secondaryDialog,
                viewModel::dismissSpellListSecondaryDialog
            ) { viewModel.castSpell(it) }

        is ISpellListDialogDialogs.ConfirmSpellRemovalDialog ->
            ConfirmSpellRemovalFromListDialog(
                dialog.secondaryDialog,
                viewModel::dismissSpellListSecondaryDialog
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
    ) { ListItem(headlineContent = { Text("${stringResource(Res.string.level)} $it") }) }
}

@Composable
private fun ConfirmSpellRemovalFromListDialog(
    dialog: ISpellListDialogDialogs.ConfirmSpellRemovalDialog,
    onDialogDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    ConfirmationDialog(onConfirm, onDialogDismiss, dialog.title.text())
}
