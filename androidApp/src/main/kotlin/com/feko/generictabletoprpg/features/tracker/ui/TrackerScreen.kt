package com.feko.generictabletoprpg.features.tracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.common.ui.components.AddFABButtonWithDropdown
import com.feko.generictabletoprpg.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.common.ui.components.OverviewListItem
import com.feko.generictabletoprpg.common.ui.components.SearchableReorderableLazyList
import com.feko.generictabletoprpg.common.ui.components.ToastMessage
import com.feko.generictabletoprpg.common.ui.viewmodel.ResultViewModel
import com.feko.generictabletoprpg.features.searchall.ui.getUniqueListItemKey
import com.feko.generictabletoprpg.search_everywhere
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringText.Companion.asText
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parameterSetOf
import sh.calvin.reorderable.ReorderableCollectionItemScope

@Composable
fun TrackerScreen(
    groupId: Long,
    groupName: String,
    onNavigationIconClick: () -> Unit,
    spellSelectionResultViewModel: ResultViewModel<Long>,
    onNavigateToSimpleSpellDetailsScreen: (Spell) -> Unit,
    onOpenDetails: (Any) -> Unit,
    onSelectSpellRequest: () -> Unit
) {
    val viewModel: TrackerViewModel =
        koinViewModel(parameters = { parameterSetOf(groupId, groupName) })
    spellSelectionResultViewModel.selectionResult?.let {
        spellSelectionResultViewModel.consumeSelectionResult()
        viewModel.addSpellToList(it)
    }
    Scaffold(
        topBar = {
            GttrpgTopAppBar(
                groupName.asText(),
                onNavigationIconClick
            ) {
                IconButton(onClick = { viewModel.refreshAllRequested() }) {
                    Icon(Icons.Default.Refresh, "")
                }
            }
        },
        floatingActionButton = {
            val expanded by viewModel.fabDropdown.isMenuExpanded.collectAsState(false)
            AddFABButtonWithDropdown(
                expanded = expanded,
                onDismissRequest = { viewModel.fabDropdown.dismiss() },
                onFabClicked = { viewModel.fabDropdown.toggleFabDropdownRequested() }
            ) { DropdownMenuContent { type, context -> viewModel.showCreateDialog(type) } }
        }
    ) { paddingValues ->
        SearchableReorderableLazyList(
            viewModel = viewModel,
            listItem = { item, isDragged, scope ->
                TrackerListItem(
                    item,
                    isDragged,
                    scope,
                    viewModel,
                    onOpenDetails,
                    onSelectSpellRequest
                )
            },
            Modifier.padding(paddingValues),
            addFabButtonSpacerToList = true,
            uniqueListItemKey = { getUniqueListItemKey(it) },
            onItemReordered = { from, to -> viewModel.itemReordered(from.index, to.index) },
            searchFieldHint = Res.string.search_everywhere.asText(),
            addHorizontalDivider = false
        )
    }
    ToastMessage(viewModel.toast)
    TrackerAlertDialog(viewModel, onNavigateToSimpleSpellDetailsScreen)
}

@Composable
fun LazyItemScope.TrackerListItem(
    item: Any,
    isDragged: Boolean,
    scope: ReorderableCollectionItemScope,
    viewModel: TrackerViewModel,
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
                SpellListItem(isDragged, item, scope, viewModel, onSelectSpellRequest)

            TrackedThing.Type.Text -> TextListItem(isDragged, item, scope, viewModel)
            TrackedThing.Type.HitDice -> HitDiceListItem(isDragged, item, scope, viewModel)
            TrackedThing.Type.FiveEStats -> StatsListItem(isDragged, item, scope, viewModel)
        }
    } else {
        OverviewListItem(item, Modifier.clickable(onClick = { onOpenDetails(item) }))
    }
}