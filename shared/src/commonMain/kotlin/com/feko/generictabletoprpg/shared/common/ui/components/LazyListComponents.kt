package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemInfo
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import com.feko.generictabletoprpg.shared.common.ui.theme.columnCount
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.OverviewViewModel
import com.feko.generictabletoprpg.shared.features.spell.Spell
import org.jetbrains.compose.resources.stringResource
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyStaggeredGridState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <TViewModel, T> SearchableLazyItems(
    viewModel: TViewModel,
    item: @Composable LazyStaggeredGridItemScope.(T) -> Unit,
    modifier: Modifier = Modifier,
    addFabButtonSpacer: Boolean = false,
    uniqueItemKey: (Any) -> Any = { (it as IIdentifiable).id },
    searchFieldHint: IText = Res.string.search.asText(),
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    BoxWithConstraints {
        SearchableLazyItemsLayout(
            viewModel,
            modifier,
            searchFieldHint
        ) { searchString ->
            val items by viewModel.items.collectAsState(emptyList())
            if (items.isNotEmpty()) {
                val gridState = rememberLazyStaggeredGridState()
                LazyVerticalStaggeredGrid(
                    StaggeredGridCells.Fixed(columnCount(maxWidth)),
                    Modifier.fillMaxSize(),
                    gridState,
                    contentPadding = PaddingValues(bottom = if (addFabButtonSpacer) 40.dp else 0.dp),
                    verticalItemSpacing = LocalDimens.current.gapSmall,
                    horizontalArrangement = Arrangement.spacedBy(LocalDimens.current.gapSmall)
                ) {
                    items(items, key = uniqueItemKey) { item -> item(item) }
                }
                val scrollToEnd by viewModel.scrollToEnd.collectAsState(false)
                if (scrollToEnd) {
                    LaunchedEffect(key1 = searchString) {
                        gridState.scrollToItem(items.size)
                        viewModel.consumeScrollToEndEvent()
                    }
                }
                LaunchedEffect(key1 = items) {
                    gridState.scrollToItem(0)
                }
            } else {
                NoItemsIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <TViewModel, T> SearchableReorderableLazyItems(
    viewModel: TViewModel,
    item: @Composable LazyStaggeredGridItemScope.(T, Boolean, ReorderableCollectionItemScope) -> Unit,
    modifier: Modifier = Modifier,
    addFabButtonSpacer: Boolean = false,
    uniqueItemKey: (Any) -> Any = { (it as IIdentifiable).id },
    onItemReordered: (LazyStaggeredGridItemInfo, LazyStaggeredGridItemInfo) -> Unit = { _, _ -> },
    searchFieldHint: IText = Res.string.search.asText()
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    BoxWithConstraints {
        SearchableLazyItemsLayout(
            viewModel,
            modifier,
            searchFieldHint,
        ) { searchString ->
            val items by viewModel.items.collectAsState(emptyList())
            val hapticFeedback = LocalHapticFeedback.current
            if (items.isNotEmpty()) {
                val gridState = rememberLazyStaggeredGridState()
                val state =
                    rememberReorderableLazyStaggeredGridState(gridState, onMove = { from, to ->
                        onItemReordered(from, to)
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
                    })
                LazyVerticalStaggeredGrid(
                    StaggeredGridCells.Fixed(columnCount(maxWidth)),
                    Modifier.fillMaxSize(),
                    gridState,
                    contentPadding = PaddingValues(bottom = if (addFabButtonSpacer) 40.dp else 0.dp),
                    verticalItemSpacing = LocalDimens.current.gapSmall,
                    horizontalArrangement = Arrangement.spacedBy(LocalDimens.current.gapSmall)
                ) {
                    items(
                        items,
                        key = uniqueItemKey
                    ) { item ->
                        ReorderableItem(state, uniqueItemKey(item)) { isDragging ->
                            item(item, isDragging, this)
                        }
                    }
                }
                val scrollToEnd by viewModel.scrollToEnd.collectAsState(false)
                if (scrollToEnd) {
                    LaunchedEffect(key1 = searchString) {
                        gridState.scrollToItem(items.size)
                        viewModel.consumeScrollToEndEvent()
                    }
                }
                LaunchedEffect(key1 = searchString) {
                    gridState.scrollToItem(0)
                }
            } else {
                NoItemsIndicator()
            }
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
private fun <TViewModel, T> SearchableLazyItemsLayout(
    viewModel: TViewModel,
    modifier: Modifier = Modifier,
    searchFieldHint: IText = Res.string.search.asText(),
    content: @Composable (String) -> Unit
) where TViewModel : OverviewViewModel<T>,
        T : Any {
    val searchString by viewModel.searchString.collectAsState("")
    val dimens = LocalDimens.current
    Column(
        modifier = modifier
            .padding(horizontal = dimens.paddingSmall)
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
            content(searchString)
            val isLoadingShown by viewModel.isLoadingShown.collectAsState()
            StableAnimatedVisibility(isLoadingShown) {
                FillingLoadingIndicator()
            }
        }
    }
}

@Composable
fun NoItemsIndicator() {
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
fun <T> LazyStaggeredGridItemScope.OverviewItem(item: T, modifier: Modifier = Modifier) {
    Card(shape = MaterialTheme.shapes.extraLarge) {
        ListItem(
            headlineContent = { Text((item as INamed).name) },
            supportingContent = {
                if (item is Spell) {
                    Text("${stringResource(Res.string.level)} ${item.level}, ${item.school}")
                }
            },
            modifier = modifier.animateItem(),
            colors = ListItemDefaults.colors(containerColor = CardDefaults.cardColors().containerColor)
        )
    }
}