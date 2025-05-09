package com.feko.generictabletoprpg.tracker

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.asSignedString
import com.feko.generictabletoprpg.filters.SpellFilter
import com.feko.generictabletoprpg.filters.index
import com.feko.generictabletoprpg.theme.Typography
import com.feko.generictabletoprpg.tracker.actions.AbilityActions
import com.feko.generictabletoprpg.tracker.actions.HealthActions
import com.feko.generictabletoprpg.tracker.actions.HitDiceActions
import com.feko.generictabletoprpg.tracker.actions.NumberActions
import com.feko.generictabletoprpg.tracker.actions.PercentageActions
import com.feko.generictabletoprpg.tracker.actions.SpellListActions
import com.feko.generictabletoprpg.tracker.actions.SpellSlotActions
import com.feko.generictabletoprpg.tracker.actions.StatsActions
import com.feko.generictabletoprpg.tracker.actions.TextActions
import com.ramcosta.composedestinations.generated.destinations.SearchAllScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState

@Composable
fun TrackedThingListItem(
    isDragged: Boolean,
    onItemClicked: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val targetElevation: Dp
    val targetScale: Float
    if (isDragged) {
        targetElevation = 16.dp
        targetScale = 1.05f
    } else {
        targetElevation = 0.dp
        targetScale = 1f
    }
    val elevation =
        animateDpAsState(targetElevation, label = "Tracked thing dragging elevation")
    val scale =
        animateFloatAsState(targetScale, label = "Tracked thing dragging scale")
    Card(
        onClick = onItemClicked,
        Modifier
            .fillMaxWidth()
            .scale(scale.value)
            .shadow(elevation.value)
    ) {
        content()
    }
}

@Composable
private fun DefaultTrackableLayout(
    trackableName: String,
    reorderableLazyListState: ReorderableLazyListState,
    valuePreview: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        ReorderHandle(reorderableLazyListState)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            )
        ) {
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) firstRow@{
                Text(
                    trackableName,
                    style = Typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (valuePreview == null) return@firstRow
                Box(
                    Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .padding(vertical = 4.dp)
                        .background(Color.Gray)
                )
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(85.dp)
                ) {
                    valuePreview()
                }
            }
            content()
        }
    }
}

@Composable
fun AbilityListItem(
    isDragged: Boolean,
    ability: Ability,
    reorderableLazyListState: ReorderableLazyListState,
    viewModel: TrackerViewModel,
) {
    TrackedThingListItem(isDragged) {
        DefaultTrackableLayout(
            ability.name,
            reorderableLazyListState,
            valuePreview = {
                Text(ability.getPrintableValue())
                Text(ability.type.name, style = Typography.bodySmall)
            }
        ) {
            AbilityActions(
                canSubtract = ability.canSubtract(),
                onSubtractClicked = { viewModel.useAbility(ability) },
                canRefresh = ability.canAdd(),
                onRefreshClicked = { viewModel.resetValueToDefault(ability) },
                onEditButtonClicked = { viewModel.showEditDialog(ability) },
                onDeleteButtonClicked = { viewModel.deleteItemRequested(ability) }
            )
        }
    }
}

@Composable
fun HitDiceListItem(
    isDragged: Boolean,
    hitDice: HitDice,
    reorderableLazyListState: ReorderableLazyListState,
    viewModel: TrackerViewModel
) {
    TrackedThingListItem(isDragged) {
        DefaultTrackableLayout(
            hitDice.name,
            reorderableLazyListState,
            valuePreview = {
                Text(hitDice.getPrintableValue())
                Text(hitDice.type.name, style = Typography.bodySmall)
            }
        ) {
            HitDiceActions(
                canSubtract = hitDice.canSubtract(),
                onSubtractClicked = { viewModel.useHitDie(hitDice) },
                canAdd = hitDice.canAdd(),
                onAddClicked = { viewModel.restoreHitDie(hitDice) },
                onEditButtonClicked = { viewModel.showEditDialog(hitDice) },
                onDeleteButtonClicked = { viewModel.deleteItemRequested(hitDice) }
            )
        }
    }
}

@Composable
fun PercentageListItem(
    isDragged: Boolean,
    percentage: Percentage,
    reorderableLazyListState: ReorderableLazyListState,
    viewModel: TrackerViewModel
) {
    TrackedThingListItem(isDragged) {
        DefaultTrackableLayout(
            percentage.name,
            reorderableLazyListState,
            valuePreview = {
                Text(percentage.getPrintableValue())
            }
        ) {
            PercentageActions(
                percentage,
                onAddButtonClicked = { viewModel.addToPercentageRequested(percentage) },
                onSubtractButtonClicked = { viewModel.subtractFromPercentageRequested(percentage) },
                onEditButtonClicked = { viewModel.showEditDialog(percentage) },
                onDeleteButtonClicked = { viewModel.deleteItemRequested(percentage) },
            )
        }
    }
}

@Composable
fun NumberListItem(
    isDragged: Boolean,
    number: Number,
    reorderableLazyListState: ReorderableLazyListState,
    viewModel: TrackerViewModel
) {
    TrackedThingListItem(isDragged) {
        DefaultTrackableLayout(
            number.name,
            reorderableLazyListState,
            valuePreview = {
                Text(number.getPrintableValue())
                Text(number.type.name, style = Typography.bodySmall)
            }
        ) {
            NumberActions(
                number,
                onAddButtonClicked = { viewModel.addToNumberRequested(number) },
                onSubtractButtonClicked = { viewModel.subtractFromNumberRequested(number) },
                onEditButtonClicked = { viewModel.showEditDialog(number) },
                onDeleteButtonClicked = { viewModel.deleteItemRequested(number) },
            )
        }
    }
}

@Composable
fun HealthListItem(
    isDragged: Boolean,
    health: Health,
    reorderableLazyListState: ReorderableLazyListState,
    viewModel: TrackerViewModel
) {
    TrackedThingListItem(isDragged) {
        DefaultTrackableLayout(
            health.name,
            reorderableLazyListState,
            valuePreview = {
                Text(health.getPrintableValue())
                if (health.temporaryHp > 0) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(R.drawable.shield_with_heart),
                            "",
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            health.temporaryHp.toString(),
                            style = Typography.bodySmall
                        )
                    }
                }
                Text(health.type.name, style = Typography.bodySmall)
            }
        ) {
            HealthActions(
                health,
                onHealButtonClicked = { viewModel.healRequested(health) },
                onDamageButtonClicked = { viewModel.takeDamageRequested(health) },
                onAddTemporaryHpButtonClicked = { viewModel.addTemporaryHpRequested(health) },
                onResetButtonClicked = { viewModel.resetValueToDefault(health) },
                onEditButtonClicked = { viewModel.showEditDialog(health) },
                onDeleteButtonClicked = { viewModel.deleteItemRequested(health) },
            )
        }
    }
}

@Composable
fun SpellListItem(
    isDragged: Boolean,
    spellList: SpellList,
    reorderableLazyListState: ReorderableLazyListState,
    navigator: DestinationsNavigator,
    viewModel: TrackerViewModel
) {
    TrackedThingListItem(
        isDragged,
        onItemClicked = {
            viewModel.showPreviewSpellListDialog(spellList, resetListState = true)
        }) {
        SpellListItemContent(
            spellList,
            reorderableLazyListState,
            onListButtonClicked = {
                viewModel.showPreviewSpellListDialog(
                    spellList,
                    resetListState = true
                )
            },
            onAddButtonClicked = {
                viewModel.addingSpellToList(spellList)
                navigator.navigate(
                    SearchAllScreenDestination(
                        SpellFilter().index(), true
                    )
                )
            },
            onEditButtonClicked = { viewModel.showEditDialog(spellList) },
            onDeleteButtonClicked = { viewModel.deleteItemRequested(spellList) }
        )
    }
}

@Composable
fun SpellListItemContent(
    spellList: SpellList,
    reorderableLazyListState: ReorderableLazyListState,
    onListButtonClicked: () -> Unit,
    onAddButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit
) {
    DefaultTrackableLayout(
        spellList.name,
        reorderableLazyListState,
        valuePreview = {
            Text(spellList.getPrintableValue())
            Text(spellList.type.name, style = Typography.bodySmall)
        }
    ) {
        SpellListActions(
            isListButtonEnabled = spellList.serializedItem.any(),
            onListButtonClicked = onListButtonClicked,
            onAddButtonClicked = onAddButtonClicked,
            onEditButtonClicked = onEditButtonClicked,
            onDeleteButtonClicked = onDeleteButtonClicked
        )
    }
}

@Composable
fun SpellSlotListItem(
    isDragged: Boolean,
    spellSlot: SpellSlot,
    reorderableLazyListState: ReorderableLazyListState,
    viewModel: TrackerViewModel
) {
    TrackedThingListItem(isDragged) {
        DefaultTrackableLayout(
            spellSlot.name,
            reorderableLazyListState,
            valuePreview = {
                Text(spellSlot.getPrintableValue())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) row@{
                    Text(spellSlot.type.name, style = Typography.bodySmall)
                    Text("Lv ${spellSlot.level}", style = Typography.bodySmall)
                }
            }
        ) {
            SpellSlotActions(
                canSubtract = spellSlot.canSubtract(),
                onSubtractClicked = { viewModel.useSpell(spellSlot) },
                canRefresh = spellSlot.canAdd(),
                onRefreshClicked = { viewModel.resetValueToDefault(spellSlot) },
                onEditButtonClicked = { viewModel.showEditDialog(spellSlot) },
                onDeleteButtonClicked = { viewModel.deleteItemRequested(spellSlot) }
            )
        }
    }
}

@Composable
fun StatsListItem(
    isDragged: Boolean,
    stats: Stats,
    reorderableLazyListState: ReorderableLazyListState,
    viewModel: TrackerViewModel
) {
    TrackedThingListItem(
        isDragged,
        onItemClicked = { viewModel.showStatsDialog(stats) }) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(
                    start = 0.dp,
                    top = 16.dp,
                    end = 16.dp
                )
            ) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ReorderHandle(
                        reorderableLazyListState,
                        Modifier.padding(end = 8.dp),
                        iconAlignment = Alignment.TopCenter
                    )
                    Text(
                        stats.name,
                        style = Typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                }
                StatsOverview(stats)
                StatsActions(
                    stats,
                    onPreviewButtonClicked = { viewModel.showEditDialog(stats) },
                    onEditButtonClicked = { viewModel.showEditDialog(stats) },
                    onDeleteButtonClicked = { viewModel.deleteItemRequested(stats) }
                )
            }
        }
    }
}

@Composable
fun TextListItem(
    isDragged: Boolean,
    text: Text,
    reorderableLazyListState: ReorderableLazyListState,
    viewModel: TrackerViewModel
) {
    TrackedThingListItem(isDragged) {
        DefaultTrackableLayout(
            text.name,
            reorderableLazyListState
        ) {
            var canTextBeExpanded by remember { mutableStateOf(false) }
            var expanded by remember { mutableStateOf(false) }
            val linesShownByDefault = 3
            Text(
                text.getPrintableValue(),
                Modifier.clickable(enabled = canTextBeExpanded) {
                    expanded = !expanded
                },
                maxLines = if (expanded) Int.MAX_VALUE else linesShownByDefault,
                style = Typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.lineCount > linesShownByDefault
                        || textLayoutResult.lineCount == linesShownByDefault
                        && textLayoutResult.isLineEllipsized(2)
                    ) {
                        canTextBeExpanded = true
                    } else {
                        canTextBeExpanded = false
                        expanded = false
                    }
                }
            )
            TextActions(
                canTextBeExpanded,
                expanded,
                onExpandStateChanged = { expanded = it },
                onEditButtonClicked = { viewModel.showEditDialog(text) },
                onDeleteButtonClicked = { viewModel.deleteItemRequested(text) }
            )
        }
    }
}

@Composable
private fun ReorderHandle(
    state: ReorderableLazyListState,
    modifier: Modifier = Modifier,
    iconAlignment: Alignment = Alignment.Center
) {
    Box(
        Modifier
            .fillMaxHeight()
            .padding(8.dp)
            .detectReorder(state)
            .then(modifier)
    ) {
        Icon(
            Icons.Default.Menu,
            "",
            Modifier.align(iconAlignment)
        )
    }
}

@Composable
fun StatsOverview(stats: Stats) {
    val statValue = requireNotNull(stats.serializedItem)
    Column(Modifier.padding(vertical = 8.dp)) {
        Row(Modifier.fillMaxWidth()) {
            CompactStat(
                statValue.proficiencyBonus.asSignedString(),
                stringResource(R.string.proficiency_bonus),
                modifier = Modifier.weight(1f)
            )
            CompactStat(
                statValue.initiative.asSignedString(),
                stringResource(R.string.initiative),
                modifier = Modifier.weight(1f)
            )
        }
        if (statValue.stats.any { it.isSpellcastingModifier }) {
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween
            ) {
                CompactStat(
                    statValue.spellSaveDc.toString(),
                    stringResource(R.string.spell_save_dc),
                    modifier = Modifier.weight(1f)
                )
                CompactStat(
                    statValue.spellAttackBonus.asSignedString(),
                    stringResource(R.string.spell_attack_bonus),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        val statsPerRow = 3
        for (row in 0..<statValue.stats.size step statsPerRow) {
            Row {
                for (itemInRow in 0..<statsPerRow) {
                    val statIndex = row + itemInRow
                    if (statIndex >= statValue.stats.size) {
                        break
                    }
                    val stat = statValue.stats[statIndex]
                    CompactStat(
                        stat.score.toString(),
                        stat.bonus.asSignedString(),
                        Modifier.weight(1f),
                        stat.shortName,
                        stat.isSpellcastingModifier
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun StatsOverviewPreview() {
    Card {
        StatsOverview(
            Stats(name = "Stats", value = "[]").also {
                it.serializedItem = StatsContainer(
                    proficiencyBonus = 1,
                    spellSaveDc = 3,
                    spellSaveDcAdditionalBonus = 0,
                    spellAttackBonus = 4,
                    spellAttackAdditionalBonus = 0,
                    initiative = 2,
                    initiativeAdditionalBonus = 0,
                    stats = createDefault5EStatEntries(LocalContext.current),
                    use5eCalculations = true
                )
            }
        )
    }
}

@Composable
fun CompactStat(
    statValue: String,
    bottomText: String,
    modifier: Modifier = Modifier,
    topText: String? = null,
    isSpellcastingModifier: Boolean = false
) {
    Box(
        Modifier
            .padding(8.dp)
            .then(modifier),
        Alignment.Center
    ) {
        Box {
            Column(
                Modifier.padding(horizontal = 16.dp),
                Arrangement.spacedBy(4.dp),
                Alignment.CenterHorizontally
            ) {
                if (topText != null) {
                    Text(topText, style = Typography.titleSmall)
                }
                Text(statValue, style = Typography.titleLarge)
                Text(
                    bottomText,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = Typography.bodySmall
                )
            }
            if (isSpellcastingModifier) {
                Icon(
                    painterResource(R.drawable.book_4_spark),
                    "",
                    Modifier
                        .padding(top = 4.dp)
                        .size(12.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Preview
@Composable
fun CompactStatPreview() {
    Card {
        CompactStat(
            "13",
            "+1",
            topText = "STR",
        )
    }
}

@Preview
@Composable
private fun SpellListTrackedThingPreview() {
    TrackedThingListItem(
        isDragged = false,
        onItemClicked = { }) {
        SpellListItemContent(
            SpellList(0, "Spell List", "", 0, 0),
            rememberReorderableLazyListState(onMove = { _, _ -> }),
            onListButtonClicked = {},
            onAddButtonClicked = {},
            onEditButtonClicked = {},
            onDeleteButtonClicked = {}
        )
    }
}

