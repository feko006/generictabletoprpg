package com.feko.generictabletoprpg.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.IIdentifiable
import com.feko.generictabletoprpg.common.INamed
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.theme.Typography
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ItemPosition
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <TViewModel, T> OverviewScreen(
    viewModel: TViewModel,
    listItem: @Composable (T, Boolean, ReorderableLazyListState?) -> Unit,
    uniqueListItemKey: (Any) -> Any = { (it as IIdentifiable).id },
    fabButton: @Composable ((Modifier) -> Unit)? = null,
    isAlertDialogVisible: Boolean = false,
    alertDialogComposable: @Composable () -> Unit = {},
    isReorderable: Boolean = false,
    onItemReordered: (ItemPosition, ItemPosition) -> Unit = { _, _ -> },
    @StringRes
    searchFieldHintResource: Int = R.string.search,
    isBottomSheetVisible: Boolean = false,
    onBottomSheetHidden: () -> Unit = {},
    bottomSheetContent: @Composable () -> Unit = {}
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    val listItems by viewModel.items.collectAsState(listOf())
    val searchString by viewModel.searchString.collectAsState("")
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchTextField(
            searchString, {
                viewModel.searchStringUpdated(it)
            },
            searchFieldHintResource
        )
        if (listItems.isNotEmpty()) {
            val listState: LazyListState
            if (isReorderable) {
                val state = rememberReorderableLazyListState(onMove = onItemReordered)
                listState = state.listState
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .reorderable(state)
                        .detectReorderAfterLongPress(state),
                    state = state.listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        listItems,
                        key = uniqueListItemKey
                    ) { item ->
                        ReorderableItem(
                            reorderableState = state,
                            key = uniqueListItemKey(item)
                        ) { isDragging ->
                            listItem(item, isDragging, state)
                        }
                    }
                    fabButtonSpacer(fabButton != null)
                }
            } else {
                listState = rememberLazyListState()
                LazyColumn(
                    Modifier.fillMaxSize(),
                    listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        listItems,
                        key = uniqueListItemKey
                    ) { item ->
                        listItem(item, false, null)
                    }
                    fabButtonSpacer(fabButton != null)
                }
            }
            LaunchedEffect(key1 = searchString) {
                listState.scrollToItem(0)
            }
        } else {
            EmptyList()
        }
    }
    if (fabButton != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        {
            fabButton(Modifier.align(Alignment.BottomEnd))
        }
    }
    if (isBottomSheetVisible) {
        val coroutineScope = rememberCoroutineScope()
        val bottomSheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    onBottomSheetHidden()
                }
            },
            sheetState = bottomSheetState
        ) {
            Box(Modifier.padding(16.dp)) {
                bottomSheetContent()
            }
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
        }
    }
    if (isAlertDialogVisible) {
        alertDialogComposable()
    }
}

fun LazyListScope.fabButtonSpacer(isFabButtonVisible: Boolean) {
    if (isFabButtonVisible) {
        item(key = "fabSpacer") {
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun EmptyList() {
    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.List,
                "",
                Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                stringResource(R.string.empty_list),
                Modifier.align(Alignment.CenterHorizontally),
                style = Typography.titleLarge
            )
        }
    }
}

@Composable
fun <T> OverviewListItem(
    item: T,
    navigateToDetails: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text((item as INamed).name)
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = navigateToDetails)
    )
}