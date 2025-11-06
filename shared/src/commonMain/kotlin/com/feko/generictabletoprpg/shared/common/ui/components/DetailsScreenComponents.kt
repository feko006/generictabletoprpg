package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.DetailsViewModel

@Composable
fun <TViewModel, T> DetailsScreen(
    id: Long,
    viewModel: TViewModel,
    title: IText,
    onNavigationIconClick: () -> Unit,
    screenContent: @Composable ColumnScope.(T) -> Unit
) where TViewModel : DetailsViewModel<T> {
    val screenState by viewModel.screenState.collectAsState(
        DetailsViewModel.DetailsScreenState.Loading
    )
    viewModel.itemIdChanged(id)
    when (screenState) {
        is DetailsViewModel.DetailsScreenState.Loading -> FillingLoadingIndicator()

        is DetailsViewModel.DetailsScreenState.ItemReady<*> -> {
            @Suppress("UNCHECKED_CAST")
            val readiedItem =
                screenState as DetailsViewModel.DetailsScreenState.ItemReady<T>
            Scaffold(
                topBar = { GttrpgTopAppBar(title, onNavigationIconClick) }
            ) { paddingValues ->
                DetailsScreen(readiedItem.item, Modifier.padding(paddingValues), screenContent)
            }
        }
    }
}

@Composable
fun <T> DetailsScreen(
    item: T,
    modifier: Modifier,
    screenContent: @Composable ColumnScope.(T) -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Column(
            modifier.then(
                LocalDimens.current.paddingMedium.let {
                    Modifier.padding(start = it, end = it, bottom = it)
                }
            ),
            Arrangement.spacedBy(LocalDimens.current.gapSmall)
        ) {
            screenContent(item)
        }
    }
}