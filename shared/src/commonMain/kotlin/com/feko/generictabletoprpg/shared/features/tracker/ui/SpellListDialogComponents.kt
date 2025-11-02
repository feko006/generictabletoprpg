package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.cast
import com.feko.generictabletoprpg.casting_time
import com.feko.generictabletoprpg.dismiss
import com.feko.generictabletoprpg.level
import com.feko.generictabletoprpg.prepared
import com.feko.generictabletoprpg.remove
import com.feko.generictabletoprpg.shared.common.ui.components.AlertDialogBase
import com.feko.generictabletoprpg.shared.common.ui.components.DialogTitle
import com.feko.generictabletoprpg.shared.common.ui.components.verticalAlignBottomIcon
import com.feko.generictabletoprpg.shared.common.ui.components.verticalAlignTopIcon
import com.feko.generictabletoprpg.shared.common.ui.theme.Typography
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.tracker.model.SpellListEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun SpellListDialogWithViewModel(
    dialog: ITrackerDialog.SpellListDialog,
    viewModel: TrackerViewModel,
    onSpellClick: (Spell) -> Unit,
) {
    val availableSpellSlots by viewModel.availableSpellSlotLevels.collectAsState(listOf())
    SpellListDialog(
        dialog,
        availableSpellSlots,
        viewModel.spellListState,
        onDialogDismissed = viewModel::dismissDialog,
        onFilteringByPreparedStateChanged = { viewModel.setShowingPreparedSpells(it) },
        onSpellPreparedStateChanged = { spellListEntry, isPrepared ->
            viewModel.changeSpellListEntryPreparedState(
                dialog.spellList,
                spellListEntry,
                isPrepared
            )
        },
        onCastSpellRequested = { level -> viewModel.castSpellRequested(level) },
        onRemoveSpellRequested = { viewModel.removeSpellFromSpellListRequested(it) },
        onSpellClick = onSpellClick,
    )
    SpellListSecondaryDialog(dialog, viewModel)
}

@Composable
fun SpellListDialog(
    dialog: ITrackerDialog.SpellListDialog,
    availableSpellSlots: List<TrackedThing>,
    spellListState: LazyListState,
    onDialogDismissed: () -> Unit,
    onFilteringByPreparedStateChanged: (Boolean) -> Unit,
    onSpellPreparedStateChanged: (SpellListEntry, Boolean) -> Unit,
    onCastSpellRequested: (level: Int) -> Unit,
    onRemoveSpellRequested: (spell: SpellListEntry) -> Unit,
    onSpellClick: (Spell) -> Unit
) {
    AlertDialogBase(
        onDialogDismissed,
        dialogTitle = { DialogTitle(dialog.title.text()) },
        dialogButtons = {
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween
            ) {
                val coroutineScope = rememberCoroutineScope()
                Row {
                    if (spellListState.canScrollBackward) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    spellListState.animateScrollToItem(0)
                                }
                            }
                        ) { Icon(verticalAlignTopIcon, "") }
                    }
                    if (spellListState.canScrollForward) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    @Suppress("UNCHECKED_CAST")
                                    spellListState.animateScrollToItem((dialog.spellList.serializedItem as List<SpellListEntry>).size - 1)
                                }
                            }
                        ) { Icon(verticalAlignBottomIcon, "") }
                    }
                }
                TextButton(
                    onClick = onDialogDismissed,
                    modifier = Modifier.wrapContentWidth()
                ) { Text(stringResource(Res.string.dismiss)) }
            }
        }
    ) {
        SpellListContent(
            dialog,
            availableSpellSlots,
            spellListState,
            onFilteringByPreparedStateChanged,
            onSpellPreparedStateChanged,
            onCastSpellRequested,
            onRemoveSpellRequested,
            onSpellClick,
            Modifier.heightIn(
                0.dp,
                with(LocalDensity.current) { (LocalWindowInfo.current.containerSize.height * 0.7f).toDp() }
            )
        )
    }
}

@Composable
fun SpellListEntryListItem(
    spellListEntry: SpellListEntry,
    canCastSpell: Boolean,
    onSpellPreparedStateChanged: (Boolean) -> Unit,
    onCastSpellClicked: () -> Unit,
    onRemoveSpellRequested: () -> Unit,
    onSpellClick: (Spell) -> Unit
) {
    ElevatedCard(onClick = { onSpellClick(spellListEntry.toSpell()) }) {
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
                            stringResource(Res.string.prepared),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            style = Typography.labelSmall
                        )
                    }
                }
            }
            HorizontalDivider(Modifier.padding(horizontal = 2.dp))
            Text(
                "${stringResource(Res.string.level)} ${spellListEntry.level}, ${spellListEntry.school}",
                Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = Typography.bodySmall
            )
            Text(
                "${stringResource(Res.string.casting_time)}: ${spellListEntry.castingTime}",
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
                        Text(stringResource(Res.string.cast))
                    }
                }
                TextButton(onClick = onRemoveSpellRequested) {
                    Text(stringResource(Res.string.remove))
                }
            }
        }
    }
}
