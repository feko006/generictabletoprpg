package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.search_everywhere
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.AddFABButtonWithDropdown
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.common.ui.components.OverviewItem
import com.feko.generictabletoprpg.shared.common.ui.components.SearchableReorderableLazyItems
import com.feko.generictabletoprpg.shared.common.ui.components.ToastMessage
import com.feko.generictabletoprpg.shared.common.ui.components.refreshIcon
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.ResultViewModel
import com.feko.generictabletoprpg.shared.features.searchall.ui.getUniqueListItemKey
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import sh.calvin.reorderable.ReorderableCollectionItemScope

@Composable
fun TrackerScreen(
    viewModel: TrackerViewModel,
    onNavigationIconClick: () -> Unit,
    spellSelectionResultViewModel: ResultViewModel<Long>,
    onNavigateToSpellListScreen: () -> Unit,
    onNavigateToSimpleSpellDetailsScreen: (Spell) -> Unit,
    onOpenDetails: (Any) -> Unit,
    onSelectSpellRequest: () -> Unit
) {
    spellSelectionResultViewModel.selectionResult?.let {
        spellSelectionResultViewModel.consumeSelectionResult()
        viewModel.addSpellToList(it)
    }
    Scaffold(
        topBar = {
            GttrpgTopAppBar(
                viewModel.groupName.asText(),
                onNavigationIconClick
            ) {
                IconButton(onClick = { viewModel.refreshAllRequested() }) {
                    Icon(refreshIcon, "")
                }
            }
        },
        floatingActionButton = {
            val expanded by viewModel.fabDropdown.isMenuExpanded.collectAsState(false)
            AddFABButtonWithDropdown(
                expanded = expanded,
                onDismissRequest = { viewModel.fabDropdown.dismiss() },
                onFabClicked = { viewModel.fabDropdown.toggleFabDropdownRequested() }
            ) { DropdownMenuContent { type -> viewModel.showCreateDialog(type) } }
        }
    ) { paddingValues ->
        SearchableReorderableLazyItems(
            viewModel = viewModel,
            item = { item, isDragged, scope ->
                TrackerListItem(
                    item,
                    isDragged,
                    scope,
                    viewModel,
                    onNavigateToSpellListScreen,
                    onOpenDetails,
                    onSelectSpellRequest
                )
            },
            Modifier.padding(paddingValues),
            addFabButtonSpacer = true,
            uniqueItemKey = { getUniqueListItemKey(it) },
            onItemReordered = { from, to -> viewModel.itemReordered(from.index, to.index) },
            searchFieldHint = Res.string.search_everywhere.asText()
        )
    }
    val toastMessage by viewModel.toast.collectAsState(null)
    ToastMessage(toastMessage)
    TrackerAlertDialog(viewModel, onNavigateToSimpleSpellDetailsScreen)
}

@Composable
fun LazyStaggeredGridItemScope.TrackerListItem(
    item: Any,
    isDragged: Boolean,
    scope: ReorderableCollectionItemScope,
    viewModel: TrackerViewModel,
    onNavigateToSpellListScreen: () -> Unit,
    onOpenDetails: (Any) -> Unit,
    onSelectSpellRequest: () -> Unit
) {
    if (item is TrackedThing) {
        when (item.type) {
            TrackedThing.Type.None -> Unit
            TrackedThing.Type.Percentage -> PercentageListItem(isDragged, item, scope, viewModel)
            TrackedThing.Type.Health -> HealthListItem(isDragged, item, scope, viewModel)
            TrackedThing.Type.Ability -> AbilityListItem(isDragged, item, scope, viewModel)
            TrackedThing.Type.SpellSlot -> SpellSlotListItem(isDragged, item, scope, viewModel)
            TrackedThing.Type.Number -> NumberListItem(isDragged, item, scope, viewModel)
            TrackedThing.Type.SpellList ->
                SpellListItem(
                    isDragged,
                    item,
                    scope,
                    viewModel,
                    onNavigateToSpellListScreen,
                    onSelectSpellRequest
                )

            TrackedThing.Type.Text -> TextListItem(isDragged, item, scope, viewModel)
            TrackedThing.Type.HitDice -> HitDiceListItem(isDragged, item, scope, viewModel)
            TrackedThing.Type.FiveEStats -> StatsListItem(isDragged, item, scope, viewModel)
        }
    } else {
        OverviewItem(item, Modifier.clickable(onClick = { onOpenDetails(item) }))
    }
}