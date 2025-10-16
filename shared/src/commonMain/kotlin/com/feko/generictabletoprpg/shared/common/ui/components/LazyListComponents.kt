package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.empty_list
import com.feko.generictabletoprpg.level
import com.feko.generictabletoprpg.search
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.common.ui.theme.Typography
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.OverviewViewModel
import com.feko.generictabletoprpg.shared.features.spell.Spell
import org.jetbrains.compose.resources.stringResource
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <TViewModel, T> SearchableLazyList(
    viewModel: TViewModel,
    listItem: @Composable LazyItemScope.(T) -> Unit,
    modifier: Modifier = Modifier,
    addFabButtonSpacerToList: Boolean = false,
    uniqueListItemKey: (Any) -> Any = { (it as IIdentifiable).id },
    searchFieldHint: IText = Res.string.search.asText(),
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    SearchableLazyListLayout(
        viewModel,
        modifier,
        searchFieldHint
    ) { searchString ->
        val listItems by viewModel.items.collectAsState(emptyList())
        if (listItems.isNotEmpty()) {
            val listState: LazyListState = rememberLazyListState()
            LazyColumn(
                Modifier.fillMaxSize(),
                listState,
                contentPadding = PaddingValues(bottom = if (addFabButtonSpacerToList) 40.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(LocalDimens.current.gapSmall)
            ) {
                items(
                    listItems,
                    key = uniqueListItemKey
                ) { item ->
                    listItem(item)
                    HorizontalDivider()
                }
            }
            val scrollToEndOfList by viewModel.scrollToEndOfList.collectAsState(false)
            if (scrollToEndOfList) {
                LaunchedEffect(key1 = searchString) {
                    listState.scrollToItem(listItems.size)
                    viewModel.consumeScrollToEndOfListEvent()
                }
            }
            LaunchedEffect(key1 = listItems) {
                listState.scrollToItem(0)
            }
        } else {
            EmptyList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <TViewModel, T> SearchableReorderableLazyList(
    viewModel: TViewModel,
    listItem: @Composable LazyItemScope.(T, Boolean, ReorderableCollectionItemScope) -> Unit,
    modifier: Modifier = Modifier,
    addFabButtonSpacerToList: Boolean = false,
    uniqueListItemKey: (Any) -> Any = { (it as IIdentifiable).id },
    onItemReordered: (LazyListItemInfo, LazyListItemInfo) -> Unit = { _, _ -> },
    searchFieldHint: IText = Res.string.search.asText(),
    addHorizontalDivider: Boolean = true,
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    SearchableLazyListLayout(
        viewModel,
        modifier,
        searchFieldHint,
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
                contentPadding = PaddingValues(bottom = if (addFabButtonSpacerToList) 40.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(LocalDimens.current.gapSmall)
            ) {
                items(
                    listItems,
                    key = uniqueListItemKey
                ) { item ->
                    ReorderableItem(state, uniqueListItemKey(item)) { isDragging ->
                        listItem(item, isDragging, this)
                    }
                    if (addHorizontalDivider) {
                        HorizontalDivider()
                    }
                }
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
private fun <TViewModel, T> SearchableLazyListLayout(
    viewModel: TViewModel,
    modifier: Modifier = Modifier,
    searchFieldHint: IText = Res.string.search.asText(),
    listContent: @Composable (String) -> Unit
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    val searchString by viewModel.searchString.collectAsState("")
    val dimens = LocalDimens.current
    Column(
        modifier = modifier
            .padding(horizontal = dimens.paddingMedium)
            .clip(
                MaterialTheme.shapes.extraLarge.copy(
                    topStart = CornerSize(0.dp),
                    topEnd = CornerSize(0.dp)
                )
            ),
        verticalArrangement = Arrangement.spacedBy(dimens.gapSmall)
    ) {
        SearchTextField(
            searchString,
            onValueChange = {
                viewModel.searchStringUpdated(it)
            },
            searchFieldHint
        )
        Box {
            listContent(searchString)
            val isLoadingShown by viewModel.isLoadingShown.collectAsState()
            StableAnimatedVisibility(isLoadingShown) {
                FillingLoadingIndicator()
            }
        }
    }
}

@Composable
fun EmptyList() {
    Box(Modifier.fillMaxSize()) {
        val dimens = LocalDimens.current
        Column(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(dimens.gapSmall)
        ) {
            Icon(
                listIcon,
                "",
                Modifier
                    .size(dimens.visualLarge)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                stringResource(Res.string.empty_list),
                Modifier.align(Alignment.CenterHorizontally),
                style = Typography.titleLarge
            )
        }
    }
}

@Composable
fun <T> LazyItemScope.OverviewListItem(
    item: T,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text((item as INamed).name)
        },
        supportingContent = {
            if (item is Spell) {
                Text("${stringResource(Res.string.level)} ${item.level}, ${item.school}")
            }
        },
        modifier = modifier.animateItem()
    )
}