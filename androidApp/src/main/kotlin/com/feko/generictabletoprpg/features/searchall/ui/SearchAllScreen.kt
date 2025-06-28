package com.feko.generictabletoprpg.features.searchall.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.common.ui.RootDestinations
import com.feko.generictabletoprpg.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.common.ui.components.OverviewListItem
import com.feko.generictabletoprpg.common.ui.components.SearchableLazyList
import com.feko.generictabletoprpg.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.common.ui.viewmodel.ResultViewModel
import com.feko.generictabletoprpg.features.filters.asFilter
import com.feko.generictabletoprpg.features.filters.ui.Filter
import kotlinx.coroutines.launch
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
    Scaffold(
        topBar = {
            GttrpgTopAppBar(R.string.search_all_title.asText(), onNavigationIconClick) {
                val filterOffButtonVisible by viewModel.filter.offButtonVisible.collectAsState(false)
                if (filterOffButtonVisible) {
                    IconButton(onClick = { viewModel.filter.resetFilter() }) {
                        Icon(painterResource(R.drawable.filter_list_off), "")
                    }
                }
                val filterButtonVisible by viewModel.filter.isButtonVisible.collectAsState(false)
                if (filterButtonVisible) {
                    IconButton(onClick = { viewModel.filterRequested() }) {
                        Icon(painterResource(R.drawable.filter_list), "")
                    }
                }
            }
        }
    ) { paddingValues ->
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
            modifier = Modifier.padding(paddingValues),
            uniqueListItemKey = { getUniqueListItemKey(it) },
            searchFieldHint = R.string.search_everywhere.asText()
        )
    }
    val isBottomSheetVisible by viewModel.isBottomSheetVisible.collectAsState(false)
    if (isBottomSheetVisible) {
        val coroutineScope = rememberCoroutineScope()
        val bottomSheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                    viewModel.bottomSheetHidden()
                }
            },
            sheetState = bottomSheetState
        ) {
            Box(Modifier.padding(LocalDimens.current.paddingMedium)) {
                val filter = viewModel.filter.activeFilter.collectAsState()
                Filter(
                    filter.value,
                    isTypeFixed = isStartedForResult
                ) { updatedFilter ->
                    viewModel.filter.filterUpdated(updatedFilter)
                }
            }
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
        }
    }
}

fun getUniqueListItemKey(it: Any) = "${it::class}${(it as IIdentifiable).id}"