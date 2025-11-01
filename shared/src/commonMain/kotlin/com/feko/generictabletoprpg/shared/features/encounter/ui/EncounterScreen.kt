package com.feko.generictabletoprpg.shared.features.encounter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.encounters
import com.feko.generictabletoprpg.round
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.common.ui.components.NoItemsIndicator
import com.feko.generictabletoprpg.shared.common.ui.components.ToastMessage
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EncounterScreen(onNavigationIconClick: () -> Unit) {
    val viewModel: EncounterViewModel = koinViewModel()
    val entries by viewModel.entries.collectAsState(listOf())
    Scaffold(
        topBar = { GttrpgTopAppBar(Res.string.encounters.asText(), onNavigationIconClick) },
        bottomBar = { ActionButtons(viewModel) }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            if (entries.isEmpty()) {
                Box(Modifier.weight(1f)) { NoItemsIndicator() }
            } else {
                val listState = rememberLazyListState()
                LazyColumn(
                    Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .clip(MaterialTheme.shapes.extraLarge),
                    listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                LaunchedEffect(Unit) {
                    viewModel.scrollToItemWithIndex.collect {
                        listState.animateScrollToItem(it)
                    }
                }
            }
            val currentRound by viewModel.currentRound.collectAsState(0)
            if (currentRound > 0) {
                ElevatedCard(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                ) {
                    Box(Modifier.padding(8.dp)) {
                        Text("${stringResource(Res.string.round)}: $currentRound")
                    }
                }
            }
        }
    }
    EncounterAlertDialog(viewModel)
    val toastMessage by viewModel.toastMessage.collectAsState(initial = null)
    ToastMessage(toastMessage)
}