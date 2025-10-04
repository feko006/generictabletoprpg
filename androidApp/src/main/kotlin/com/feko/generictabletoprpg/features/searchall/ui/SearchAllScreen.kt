package com.feko.generictabletoprpg.features.searchall.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.common.ui.RootDestinations
import com.feko.generictabletoprpg.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.common.ui.components.OverviewListItem
import com.feko.generictabletoprpg.common.ui.components.SearchableLazyList
import com.feko.generictabletoprpg.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.common.ui.viewmodel.ResultViewModel
import com.feko.generictabletoprpg.features.filter.ui.FilterChipGroup
import com.feko.generictabletoprpg.features.filter.ui.FilterScreen
import com.feko.generictabletoprpg.search_all_title
import com.feko.generictabletoprpg.search_everywhere
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.features.filter.asFilter
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAllScreen(
    appViewModel: AppViewModel,
    onNavigationIconClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onOpenDetails: (Any) -> Unit,
    fixedFilter: Int? = null,
    resultViewModel: ResultViewModel<Long>? = null
) {
    val isStartedForResult = resultViewModel != null
    val viewModel: SearchAllViewModel = koinViewModel { parametersOf(fixedFilter?.asFilter()) }
    if (!isStartedForResult) {
        appViewModel.updateActiveDrawerItem(RootDestinations.SearchAll.destination)
    }
    val filter by viewModel.activeFilter.collectAsState()
    val dimens = LocalDimens.current
    Scaffold(
        topBar = {
            GttrpgTopAppBar(Res.string.search_all_title.asText(), onNavigationIconClick) {
                IconButton(onClick = { viewModel.filterRequested() }) {
                    Icon(Icons.Default.FilterList, "")
                }
            }
        }
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues),
            Arrangement.spacedBy(dimens.gapSmall)
        ) {
            FilterChipGroup(
                filter?.chipData,
                Modifier.padding(horizontal = dimens.paddingMedium)
            ) {
                if (it == null) viewModel.resetFilter() else viewModel.filterUpdated(it)
            }
            SearchableLazyList(
                viewModel,
                listItem = { item ->
                    OverviewListItem(
                        item,
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isStartedForResult) {
                                    resultViewModel.setSelectionResult((item as IIdentifiable).id)
                                    onNavigateBack()
                                } else {
                                    onOpenDetails(item)
                                }
                            })
                },
                uniqueListItemKey = { getUniqueListItemKey(it) },
                searchFieldHint = Res.string.search_everywhere.asText()
            )
        }
    }
    val isBottomSheetVisible by viewModel.isFilterBottomSheetVisible.collectAsState(false)
    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.bottomSheetHidden() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            FilterScreen(
                filter,
                isTypeFixed = isStartedForResult,
                onFilterUpdated = { updatedFilter ->
                    viewModel.bottomSheetHidden()
                    viewModel.filterUpdated(updatedFilter)
                },
                onFilterCleared = {
                    viewModel.bottomSheetHidden()
                    viewModel.resetFilter()
                }
            )
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
        }
    }
}

fun getUniqueListItemKey(it: Any) = "${it::class}${(it as IIdentifiable).id}"