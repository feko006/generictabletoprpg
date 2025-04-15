package com.feko.generictabletoprpg.initiative

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.initiative.InitiativeEntryEntity
import com.feko.generictabletoprpg.common.composable.EmptyList
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun InitiativeScreen(
    appViewModel: AppViewModel
) {
    appViewModel.set(
        appBarTitle = stringResource(R.string.initiative),
        navBarActions = listOf()
    )
    val viewModel: InitiativeViewModel = koinViewModel()
    val entries by viewModel.entries.collectAsState(listOf())
    Column {
        if (entries.isEmpty()) {
            Box(Modifier.weight(1f)) {
                EmptyList()
            }
        } else {
            val listState = rememberLazyListState()
            LazyColumn(
                Modifier
                    .weight(1f)
                    .padding(8.dp),
                listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(entries, key = { it.id }) {
                    InitiativeListItem(it)
                }
            }
        }
        Box(Modifier.fillMaxWidth()) {
            Row(
                Modifier.align(Alignment.Center),
                Arrangement.spacedBy(8.dp)
            ) {
                FloatingActionButton({}) {
                    Icon(Icons.Default.Add, "")
                }
                FloatingActionButton({}) {
                    Icon(Icons.Default.Refresh, "")
                }
                FloatingActionButton({}) {
                    Icon(painterResource(R.drawable.bolt), "")
                }
                FloatingActionButton({}) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "")
                }
            }
        }
    }
}

@Composable
fun InitiativeListItem(initiativeEntry: InitiativeEntryEntity) {
    val isHighlighted by remember { derivedStateOf { initiativeEntry.hasTurn } }
    Card(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground).takeIf { isHighlighted }
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .padding(horizontal = 8.dp),
                    Arrangement.spacedBy(8.dp),
                    Alignment.CenterVertically
                ) {
                    Text(
                        initiativeEntry.initiative.toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        initiativeEntry.name,
                        Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    Arrangement.SpaceAround
                ) {
                    IconAndTextWithTooltip(
                        R.drawable.heart,
                        initiativeEntry.health.toString(),
                        stringResource(R.string.health)
                    )
                    IconAndTextWithTooltip(
                        R.drawable.shield,
                        initiativeEntry.armorClass.toString(),
                        stringResource(R.string.armor_class)
                    )
                    if (initiativeEntry.hasLegendaryActions) {
                        IconAndTextWithTooltip(
                            R.drawable.bolt,
                            initiativeEntry.legendaryActions.toString(),
                            stringResource(R.string.legendary_actions)
                        )
                    }
                    if (initiativeEntry.hasSpellSaveDc) {
                        IconAndTextWithTooltip(
                            R.drawable.book_4_spark,
                            initiativeEntry.spellSaveDc.toString(),
                            stringResource(R.string.spell_save_dc)
                        )
                    }
                    if (initiativeEntry.hasSpellAttackModifier) {
                        IconAndTextWithTooltip(
                            R.drawable.wand_stars,
                            "+" + initiativeEntry.spellAttackModifier,
                            stringResource(R.string.spell_attack_modifier)
                        )
                    }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.Center
                ) {
                    val keepOnReset by remember { derivedStateOf { initiativeEntry.keepOnRefresh } }
                    IconToggleButton(keepOnReset, {
                        // TODO
                    }) {
                        if (keepOnReset) {
                            Icon(Icons.Default.Star, "")
                        } else {
                            Icon(painterResource(R.drawable.star), "")
                        }
                    }
                    IconButton(
                        onClick = {}
                    ) { Icon(Icons.Filled.Favorite, "") }
                    IconButton(
                        onClick = {}
                    ) { Icon(Icons.Filled.Edit, "") }
                    IconButton(
                        onClick = {}
                    ) { Icon(Icons.Filled.Delete, "") }
                }
            }
            if (isHighlighted) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.2f))
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun IconAndTextWithTooltip(@DrawableRes iconResource: Int, value: String, tooltipText: String) {
    val state = rememberBasicTooltipState(isPersistent = false)
    val coroutineScope = rememberCoroutineScope()
    BasicTooltipBox(
        TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            ElevatedCard {
                Text(tooltipText, Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        },
        state = state,
        Modifier.clickable {
            if (state.isVisible) {
                state.dismiss()
            } else {
                coroutineScope.launch {
                    state.show()
                }
            }
        }) {
        IconAndText(iconResource, value)
    }
}

@Composable
fun IconAndText(@DrawableRes iconResource: Int, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painterResource(iconResource), "")
        Text(value)
    }
}

@Preview
@Composable
fun InitiativeListItemPreview() {
    InitiativeListItem(
        InitiativeEntryEntity(1, "Larry", 10, 18, 19, 3, 14, 7, false, false)
    )
}