package com.feko.generictabletoprpg.common.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed
import com.feko.generictabletoprpg.common.ui.theme.Typography
import com.feko.generictabletoprpg.common.ui.viewmodel.OverviewViewModel
import com.feko.generictabletoprpg.features.spell.Spell
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TODO: Rename after repurposing to SearchableList
fun <TViewModel, T> OverviewScreen(
    viewModel: TViewModel,
    listItem: @Composable (T, Boolean, ReorderableLazyListState?) -> Unit,
    modifier: Modifier = Modifier,
    addFabButtonSpacerToList: Boolean = false,
    uniqueListItemKey: (Any) -> Any = { (it as IIdentifiable).id },
    @StringRes
    searchFieldHintResource: Int = R.string.search,
    isBottomSheetVisible: Boolean = false,
    onBottomSheetHidden: () -> Unit = {},
    bottomSheetContent: @Composable () -> Unit = {}
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    OverviewScreenLayout(
        viewModel,
        modifier,
        searchFieldHintResource,
        isBottomSheetVisible,
        onBottomSheetHidden,
        bottomSheetContent
    ) { searchString ->
        val listItems by viewModel.items.collectAsState(emptyList())
        if (listItems.isNotEmpty()) {
            val listState: LazyListState = rememberLazyListState()
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
                fabButtonSpacer(addFabButtonSpacerToList)
            }
            val scrollToEndOfList by viewModel.scrollToEndOfList.collectAsState(false)
            if (scrollToEndOfList) {
                LaunchedEffect(key1 = searchString) {
                    listState.scrollToItem(listItems.size)
                    viewModel.consumeScrollToEndOfListEvent()
                }
            }
            LaunchedEffect(key1 = searchString) {
                listState.scrollToItem(0)
            }
        } else {
            EmptyList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TODO: Rename after repurposing to SearchableList
fun <TViewModel, T> ReorderableOverviewScreen(
    viewModel: TViewModel,
    listItem: @Composable (T, Boolean, ReorderableCollectionItemScope) -> Unit,
    modifier: Modifier = Modifier,
    addFabButtonSpacerToList: Boolean = false,
    uniqueListItemKey: (Any) -> Any = { (it as IIdentifiable).id },
    onItemReordered: (LazyListItemInfo, LazyListItemInfo) -> Unit = { _, _ -> },
    @StringRes
    searchFieldHintResource: Int = R.string.search,
    isBottomSheetVisible: Boolean = false,
    onBottomSheetHidden: () -> Unit = {},
    bottomSheetContent: @Composable () -> Unit = {}
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    OverviewScreenLayout(
        viewModel,
        modifier,
        searchFieldHintResource,
        isBottomSheetVisible,
        onBottomSheetHidden,
        bottomSheetContent
    ) { searchString ->
        val listItems by viewModel.items.collectAsState(emptyList())
        val hapticFeedback = LocalHapticFeedback.current
        if (listItems.isNotEmpty()) {
            val listState = rememberLazyListState()
            val state = rememberReorderableLazyListState(listState, onMove = { from, to ->
                onItemReordered(from, to)
                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
            })
            LazyColumn(
                Modifier.fillMaxSize(),
                listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    listItems,
                    key = uniqueListItemKey
                ) { item ->
                    ReorderableItem(state, uniqueListItemKey(item)) { isDragging ->
                        listItem(item, isDragging, this)
                    }
                }
                fabButtonSpacer(addFabButtonSpacerToList)
            }
            val scrollToEndOfList by viewModel.scrollToEndOfList.collectAsState(false)
            if (scrollToEndOfList) {
                LaunchedEffect(key1 = searchString) {
                    listState.scrollToItem(listItems.size)
                    viewModel.consumeScrollToEndOfListEvent()
                }
            }
            LaunchedEffect(key1 = searchString) {
                listState.scrollToItem(0)
            }
        } else {
            EmptyList()
        }
    }
}

fun Modifier.draggableHandle(
    scope: ReorderableCollectionItemScope,
    hapticFeedback: HapticFeedback,
    interactionSource: MutableInteractionSource?
) = run {
    with(scope) {
        draggableHandle(
            onDragStarted = {
                hapticFeedback.performHapticFeedback(
                    HapticFeedbackType.GestureThresholdActivate
                )
            },
            onDragStopped = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureEnd)
            },
            interactionSource = interactionSource
        )
    }
}

fun Modifier.longPressDraggableHandle(
    scope: ReorderableCollectionItemScope,
    hapticFeedback: HapticFeedback,
    interactionSource: MutableInteractionSource?
) = run {
    with(scope) {
        longPressDraggableHandle(
            onDragStarted = {
                hapticFeedback.performHapticFeedback(
                    HapticFeedbackType.GestureThresholdActivate
                )
            },
            onDragStopped = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureEnd)
            },
            interactionSource = interactionSource
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <TViewModel, T> OverviewScreenLayout(
    viewModel: TViewModel,
    modifier: Modifier,
    @StringRes
    searchFieldHintResource: Int = R.string.search,
    isBottomSheetVisible: Boolean = false,
    onBottomSheetHidden: () -> Unit = {},
    bottomSheetContent: @Composable () -> Unit = {},
    listContent: @Composable (String) -> Unit
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    val searchString by viewModel.searchString.collectAsState("")
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchTextField(
            searchString, {
                viewModel.searchStringUpdated(it)
            },
            searchFieldHintResource
        )
        listContent(searchString)
    }
    // TODO: Remove from this screen
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
}

fun LazyListScope.fabButtonSpacer(isFabButtonVisible: Boolean) {
    if (isFabButtonVisible) {
        item(key = "fabSpacer") {
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
fun EmptyList() {
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
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors()
) {
    ListItem(
        headlineContent = {
            Text((item as INamed).name)
        },
        supportingContent = {
            if (item is Spell) {
                Text("${stringResource(R.string.level)} ${item.level}, ${item.school}")
            }
        },
        modifier = modifier,
        colors = colors
    )
}