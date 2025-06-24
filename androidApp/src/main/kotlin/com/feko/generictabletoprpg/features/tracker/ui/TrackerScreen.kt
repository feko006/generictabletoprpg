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
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.common.ui.components.AddFABButtonWithDropdown
import com.feko.generictabletoprpg.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.common.ui.components.OverviewListItem
import com.feko.generictabletoprpg.common.ui.components.SearchableReorderableLazyList
import com.feko.generictabletoprpg.common.ui.components.ToastMessage
import com.feko.generictabletoprpg.common.ui.viewmodel.ResultViewModel
import com.feko.generictabletoprpg.features.searchall.ui.getUniqueListItemKey
import com.feko.generictabletoprpg.features.spell.Spell
import com.feko.generictabletoprpg.features.tracker.domain.model.AbilityTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.GenericTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.HealthTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.HitDiceTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.JsonTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.NumberTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.PercentageTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.SpellListTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.SpellSlotTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.StatsTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.TextTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThing
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
                R.string.tracker_title.asText(),
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
                modifier = Modifier,
                onDismissRequest = { viewModel.fabDropdown.dismiss() },
                onFabClicked = { viewModel.fabDropdown.toggleFabDropdownRequested() }
            ) { DropdownMenuContent { type, context -> viewModel.showCreateDialog(type, context) } }
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
            searchFieldHint = R.string.search_everywhere.asText(),
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
        when (item) {
            is AbilityTrackedThing ->
                AbilityListItem(isDragged, item, scope, viewModel)

            is HealthTrackedThing ->
                HealthListItem(isDragged, item, scope, viewModel)

            is HitDiceTrackedThing ->
                HitDiceListItem(isDragged, item, scope, viewModel)

            is NumberTrackedThing ->
                NumberListItem(isDragged, item, scope, viewModel)

            is PercentageTrackedThing ->
                PercentageListItem(isDragged, item, scope, viewModel)

            is SpellListTrackedThing ->
                SpellListItem(isDragged, item, scope, viewModel, onSelectSpellRequest)

            is SpellSlotTrackedThing ->
                SpellSlotListItem(isDragged, item, scope, viewModel)

            is StatsTrackedThing ->
                StatsListItem(isDragged, item, scope, viewModel)

            is TextTrackedThing ->
                TextListItem(isDragged, item, scope, viewModel)

            TrackedThing.Companion.Empty,
            is GenericTrackedThing<*>,
            is JsonTrackedThing<*> -> Unit
        }
    } else {
        OverviewListItem(item, Modifier.clickable(onClick = { onOpenDetails(item) }))
    }
}