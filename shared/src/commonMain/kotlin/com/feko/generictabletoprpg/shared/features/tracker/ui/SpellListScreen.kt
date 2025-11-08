package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.tracker.model.SpellListEntry

@Composable
@Suppress("UNCHECKED_CAST")
fun SpellListScreen(
    trackerViewModel: TrackerViewModel?,
    onNavigateToSimpleSpellDetailsScreen: (Spell) -> Unit,
    onPopSpellListScreen: () -> Unit,
    onPopAll: ((NavKey) -> Boolean) -> Unit
) {
    if (trackerViewModel == null) return
    val alertDialog by trackerViewModel.spellListDialog.collectAsState(ITrackerDialog.None)
    val dereferencedDialog = alertDialog
    if (dereferencedDialog !is ITrackerDialog.SpellListDialog) return
    val spellListEntries = dereferencedDialog.spellList.serializedItem as List<SpellListEntry>
    var previousSpellList by remember { mutableStateOf(spellListEntries) }
    val removedSpell = previousSpellList.minus(spellListEntries).firstOrNull()
    LaunchedEffect(removedSpell) {
        if (removedSpell != null) {
            onPopAll {
                it is INavigationDestination.SimpleSpellDetailsDestination
                        && it.spell.name == removedSpell.name
            }
        }
    }
    @Suppress("AssignedValueIsNeverRead")
    previousSpellList = spellListEntries
    DisposableEffect(trackerViewModel) {
        onDispose {
            trackerViewModel.dismissDialog()
        }
    }
    Scaffold(
        topBar = {
            GttrpgTopAppBar(
                dereferencedDialog.title,
                onNavigationIconClick = {}
            )
        }) { paddingValues ->
        val screenAdditionalPadding = LocalDimens.current.paddingMedium
        Column(
            Modifier.padding(paddingValues)
                .padding(horizontal = screenAdditionalPadding),
            verticalArrangement = Arrangement.spacedBy(LocalDimens.current.gapSmall)
        ) {
            val availableSpellSlots
                    by trackerViewModel.availableSpellSlotLevels.collectAsState(listOf())
            SpellListContent(
                dereferencedDialog,
                availableSpellSlots,
                trackerViewModel.spellListState,
                onFilteringByPreparedStateChanged = { trackerViewModel.setShowingPreparedSpells(it) },
                onSpellPreparedStateChanged = { spellListEntry, isPrepared ->
                    trackerViewModel.changeSpellListEntryPreparedState(
                        dereferencedDialog.spellList,
                        spellListEntry,
                        isPrepared
                    )
                },
                onCastSpellRequested = { level -> trackerViewModel.castSpellRequested(level) },
                onRemoveSpellRequested = { trackerViewModel.removeSpellFromSpellListRequested(it) },
                onSpellClick = onNavigateToSimpleSpellDetailsScreen,
                showScrollIndicator = false,
                listContentBottomPadding = screenAdditionalPadding,
            )
        }
    }
    SpellListSecondaryDialog(dereferencedDialog, trackerViewModel, onPopSpellListScreen)
}