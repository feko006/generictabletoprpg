package com.feko.generictabletoprpg.shared.features.encounter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.encounters
import com.feko.generictabletoprpg.round
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.BoxWithScrollIndicator
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.common.ui.components.NoItemsIndicator
import com.feko.generictabletoprpg.shared.common.ui.components.ToastMessage
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.common.ui.theme.columnCount
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EncounterScreen(onNavigationIconClick: () -> Unit) {
    val viewModel: EncounterViewModel = koinViewModel()
    val entries by viewModel.entries.collectAsState(listOf())
    Scaffold(
        Modifier.padding(bottom = LocalDimens.current.paddingMedium),
        topBar = { GttrpgTopAppBar(Res.string.encounters.asText(), onNavigationIconClick) },
        bottomBar = { ActionButtons(viewModel) }
    ) { paddingValues ->
        BoxWithConstraints(Modifier.padding(paddingValues)) constraints@{
            val paddingSmall = LocalDimens.current.paddingSmall
            if (entries.isEmpty()) {
                NoItemsIndicator()
            } else {
                val listState = rememberLazyGridState()
                BoxWithScrollIndicator(listState, Color.Transparent) {
                    LazyVerticalGrid(
                        GridCells.Fixed(columnCount(this@constraints.maxWidth)),
                        Modifier
                            .fillMaxSize()
                            .padding(paddingSmall)
                            .clip(MaterialTheme.shapes.extraLarge),
                        listState,
                        verticalArrangement = Arrangement.spacedBy(LocalDimens.current.gapSmall),
                        horizontalArrangement = Arrangement.spacedBy(LocalDimens.current.gapSmall)
                    ) {
                        items(entries, key = { it.id }) { item ->
                            InitiativeListItem(
                                item,
                                onUpdateInitiativeRequested = { viewModel.showInitiativeDialog(item) },
                                onUpdateKeepOnReset = { viewModel.updateKeepOnReset(item, it) },
                                onHealButtonClicked = { viewModel.showHealDialog(item) },
                                onDamageButtonClicked = { viewModel.showDamageDialog(item) },
                                onDuplicateButtonClicked = { viewModel.duplicateEntry(item) },
                                onEditButtonClicked = { viewModel.showEditDialog(item) },
                                onDeleteButtonClicked = { viewModel.showDeleteDialog(item) },
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                }
                LaunchedEffect(Unit) {
                    viewModel.scrollToItemWithIndex.collect {
                        listState.animateScrollToItem(it)
                    }
                }
            }
            val currentRound by viewModel.currentRound.collectAsState(0)
            if (currentRound > 0) {
                ElevatedCard(
                    Modifier.align(Alignment.BottomCenter)
                        .padding(bottom = paddingSmall),
                    MaterialTheme.shapes.extraLarge
                ) {
                    Text(
                        "${stringResource(Res.string.round)}: $currentRound",
                        Modifier.padding(paddingSmall)
                    )
                }
            }
        }
    }
    EncounterAlertDialog(viewModel)
    val toastMessage by viewModel.toastMessage.collectAsState(initial = null)
    ToastMessage(toastMessage)
}