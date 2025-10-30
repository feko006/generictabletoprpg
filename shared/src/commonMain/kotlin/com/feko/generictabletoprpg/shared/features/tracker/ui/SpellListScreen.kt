package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.features.spell.Spell

@Composable
fun SpellListScreen(
    trackerViewModel: TrackerViewModel?,
    onNavigateToSimpleSpellDetailsScreen: (Spell) -> Unit
) {
    if (trackerViewModel == null) return
    val alertDialog by trackerViewModel.dialog.collectAsState(ITrackerDialog.None)
    val dereferencedDialog = alertDialog
    if (dereferencedDialog !is ITrackerDialog.SpellListDialog) return
    DisposableEffect(trackerViewModel) {
        onDispose {
            trackerViewModel.dismissDialog()
        }
    }
    Column(Modifier.padding(LocalDimens.current.paddingMedium)) {
        SpellListContent(
            dereferencedDialog,
            trackerViewModel.spellListState,
            onFilteringByPreparedStateChanged = { trackerViewModel.setShowingPreparedSpells(it) },
            canSpellBeCast = { level -> trackerViewModel.canCastSpell(level) },
            onSpellPreparedStateChanged = { spellListEntry, isPrepared ->
                trackerViewModel.changeSpellListEntryPreparedState(
                    dereferencedDialog.spellList,
                    spellListEntry,
                    isPrepared
                )
            },
            onCastSpellRequested = { level -> trackerViewModel.castSpellRequested(level) },
            onRemoveSpellRequested = { trackerViewModel.removeSpellFromSpellListRequested(it) },
            onSpellClick = onNavigateToSimpleSpellDetailsScreen
        )
    }
    SpellListSecondaryDialog(dereferencedDialog, trackerViewModel)
}