package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.common.DetailsViewModel

@Composable
fun <TViewModel, T> DetailsScreen(
    id: Long,
    viewModel: TViewModel,
    screenContent: @Composable (T, Dp) -> Unit
)
        where TViewModel : DetailsViewModel<T> {
    val screenState by viewModel.screenState.collectAsState(
        DetailsViewModel.DetailsScreenState.Loading
    )
    viewModel.itemIdChanged(id)
    when (screenState) {
        is DetailsViewModel.DetailsScreenState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                )
            }
        }

        is DetailsViewModel.DetailsScreenState.ItemReady<*> -> {
            @Suppress("UNCHECKED_CAST")
            val readiedItem =
                screenState as DetailsViewModel.DetailsScreenState.ItemReady<T>
            val padding = 8.dp
            Column(
                Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                screenContent(readiedItem.item, padding)
            }
        }
    }
}
