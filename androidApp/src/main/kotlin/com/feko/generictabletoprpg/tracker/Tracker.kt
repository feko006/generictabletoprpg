package com.feko.generictabletoprpg.tracker

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.ButtonState
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.composable.AddFABButtonWithDropdown
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.common.composable.OverviewScreen
import com.feko.generictabletoprpg.common.composable.ToastMessage
import com.feko.generictabletoprpg.destinations.SearchAllScreenDestination
import com.feko.generictabletoprpg.destinations.SpellDetailsScreenDestination
import com.feko.generictabletoprpg.filters.SpellFilter
import com.feko.generictabletoprpg.filters.index
import com.feko.generictabletoprpg.searchall.getNavRouteInternal
import com.feko.generictabletoprpg.searchall.getUniqueListItemKey
import com.feko.generictabletoprpg.theme.Typography
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parameterSetOf

@Destination
@Composable
fun TrackerScreen(
    groupId: Long,
    groupName: String,
    appViewModel: AppViewModel,
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<SearchAllScreenDestination, Long>
) {
    val viewModel: TrackerViewModel =
        koinViewModel(parameters = { parameterSetOf(groupId, groupName) })
    appViewModel
        .set(
            appBarTitle = stringResource(R.string.tracker_title),
            navBarActions = listOf(
                ButtonState(Icons.Default.Refresh) { viewModel.refreshAllRequested() }
            )
        )
    val isAlertDialogVisible by viewModel.alertDialog.isVisible.collectAsState(false)
    resultRecipient.onNavResult { result ->
        if (result is NavResult.Value<Long>) {
            viewModel.addSpellToList(result.value)
        }
    }
    ToastMessage(viewModel.toast)
    OverviewScreen(
        viewModel = viewModel,
        listItem = { item, isDragged, state ->
            OverviewListItem(item, isDragged, state!!, navigator)
        },
        uniqueListItemKey = {
            getUniqueListItemKey(it)
        },
        fabButton = { modifier ->
            val expanded by viewModel.fabDropdown.isMenuExpanded.collectAsState(false)
            AddFABButtonWithDropdown(
                expanded = expanded,
                modifier = modifier,
                onDismissRequest = { viewModel.fabDropdown.dismiss() },
                onFabClicked = { viewModel.fabDropdown.toggleFabDropdownRequested() }
            ) {
                DropdownMenuContent(viewModel)
            }
        },
        isAlertDialogVisible = isAlertDialogVisible,
        alertDialogComposable = {
            AlertDialogComposable(viewModel, groupName, navigator)
        },
        isReorderable = true,
        onItemReordered = { from, to ->
            viewModel.itemReordered(from.index, to.index)
        },
        searchFieldHintResource = R.string.search_everywhere
    )
}

@Composable
fun OverviewListItem(
    item: Any,
    isDragged: Boolean,
    state: ReorderableLazyListState,
    navigator: DestinationsNavigator
) {
    if (item is TrackedThing) {
        TrackedThing(item, isDragged, state, navigator)
    } else {
        com.feko.generictabletoprpg.common.composable.OverviewListItem(
            item,
            Modifier.clickable {
                navigator.navigate(getNavRouteInternal(item))
            })
    }
}

@Composable
private fun TrackedThing(
    item: TrackedThing,
    isDragged: Boolean,
    state: ReorderableLazyListState,
    navigator: DestinationsNavigator
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
        Modifier
            .fillMaxWidth()
            .scale(scale.value)
            .shadow(elevation.value)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .padding(8.dp)
                    .detectReorder(state)
            ) {
                Icon(
                    Icons.Default.Menu,
                    "",
                    Modifier.align(Alignment.Center)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp)
            ) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) firstRow@{
                    Text(
                        item.name,
                        style = Typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    if (item.type == TrackedThing.Type.Text) {
                        return@firstRow
                    }
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
                        Text(item.getPrintableValue())
                        if (item is Health) {
                            if (item.temporaryHp > 0) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painterResource(R.drawable.shield_with_heart),
                                        "",
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(item.temporaryHp.toString(), style = Typography.bodySmall)
                                }
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) row@{
                            if (item.type == TrackedThing.Type.Percentage) {
                                return@row
                            }
                            Text(item.type.name, style = Typography.bodySmall)
                            if (item is SpellSlot) {
                                Text("Lv ${item.level}", style = Typography.bodySmall)
                            }
                        }
                    }
                }
                var expanded by remember { mutableStateOf(false) }
                if (item.type == TrackedThing.Type.Text) {
                    Text(
                        item.getPrintableValue(),
                        Modifier.clickable {
                            expanded = !expanded
                        },
                        maxLines = if (expanded) Int.MAX_VALUE else 3,
                        style = Typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                when (item.type) {
                    TrackedThing.Type.Percentage -> PercentageActions(item)
                    TrackedThing.Type.Number -> NumberActions(item)
                    TrackedThing.Type.Health -> HealthActions(item)
                    TrackedThing.Type.Ability -> AbilityActions(item)
                    TrackedThing.Type.SpellSlot -> SpellSlotActions(item)
                    TrackedThing.Type.SpellList -> SpellListActions(item, navigator)
                    TrackedThing.Type.Text -> TextListActions(item, expanded) { expanded = it }
                    TrackedThing.Type.HitDice -> HitDiceActions(item)
                    TrackedThing.Type.None -> Unit
                }
            }
        }
    }
}

@Composable
private fun PercentageActions(item: TrackedThing) {
    ItemActionsBase(item) { viewModel ->
        IconButton(
            onClick = { viewModel.addToPercentageRequested(item) },
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Add, "")
        }
        IconButton(
            onClick = { viewModel.subtractFromPercentageRequested(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
    }
}

@Composable
private fun NumberActions(item: TrackedThing) {
    ItemActionsBase(item) { viewModel ->
        IconButton(
            onClick = { viewModel.addToNumberRequested(item) },
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Add, "")
        }
        IconButton(
            onClick = { viewModel.subtractFromNumberRequested(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
    }
}

@Composable
private fun HealthActions(item: TrackedThing) {
    ItemActionsBase(item) { viewModel ->
        IconButton(
            onClick = { viewModel.healRequested(item) },
            enabled = item.canAdd()
        ) {
            Icon(painterResource(R.drawable.heart_plus), "")
        }
        IconButton(
            onClick = { viewModel.takeDamageRequested(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.heart_minus), "")
        }
        IconButton(
            onClick = { viewModel.addTemporaryHp(item) },
            enabled = true
        ) {
            Icon(painterResource(R.drawable.shield_with_heart), "")
        }
        IconButton(
            onClick = { viewModel.resetValueToDefault(item) },
            enabled = item.canAdd() || (item as Health).temporaryHp > 0
        ) {
            Icon(Icons.Default.Refresh, "")
        }
    }
}

@Composable
private fun AbilityActions(item: TrackedThing) {
    ItemActionsBase(item) { viewModel ->
        IconButton(
            onClick = { viewModel.useAbility(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
        IconButton(
            onClick = { viewModel.resetValueToDefault(item) },
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Refresh, "")
        }
    }
}

@Composable
private fun SpellSlotActions(item: TrackedThing) {
    ItemActionsBase(item) { viewModel ->
        IconButton(
            onClick = { viewModel.useSpell(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
        IconButton(
            onClick = { viewModel.resetValueToDefault(item) },
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Refresh, "")
        }
    }
}

@Composable
private fun SpellListActions(item: TrackedThing, navigator: DestinationsNavigator) {
    ItemActionsBase(item) { viewModel ->
        val spellList = item as SpellList
        IconButton(
            onClick = { viewModel.showPreviewSpellListDialog(spellList) },
            enabled = spellList.spells.any()
        ) {
            Icon(Icons.AutoMirrored.Filled.List, "")
        }
        IconButton(
            onClick = {
                viewModel.addingSpellToList(spellList)
                navigator.navigate(SearchAllScreenDestination(SpellFilter().index(), true))
            },
            enabled = true
        ) {
            Icon(Icons.Default.Add, "")
        }
    }
}

@Composable
private fun TextListActions(
    item: TrackedThing,
    expanded: Boolean,
    onExpandStateChanged: (Boolean) -> Unit
) {
    ItemActionsBase(item) {
        IconButton(
            onClick = {
                onExpandStateChanged(!expanded)
            },
            enabled = true
        ) {
            if (expanded) {
                Icon(Icons.Default.KeyboardArrowUp, "")
            } else {
                Icon(Icons.Default.KeyboardArrowDown, "")
            }
        }
    }
}

@Composable
private fun HitDiceActions(item: TrackedThing) {
    if (item !is IntTrackedThing) return
    ItemActionsBase(item) { viewModel ->
        IconButton(
            onClick = { viewModel.useHitDie(item) },
            enabled = item.canSubtract()
        ) {
            Icon(painterResource(R.drawable.subtract), "")
        }
        IconButton(
            onClick = { viewModel.restoreHitDie(item) },
            enabled = item.canAdd()
        ) {
            Icon(Icons.Default.Add, "")
        }
    }
}

@Composable
private fun ItemActionsBase(
    item: TrackedThing,
    actions: @Composable (TrackerViewModel) -> Unit
) {
    val viewModel = koinViewModel<TrackerViewModel>()
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        actions(viewModel)
        IconButton(
            onClick = { viewModel.showEditDialog(item) }
        ) {
            Icon(Icons.Default.Edit, "")
        }
        IconButton(
            onClick = { viewModel.deleteItemRequested(item) }
        ) {
            Icon(Icons.Default.Delete, "")
        }
    }
}

@Composable
fun DropdownMenuContent(viewModel: TrackerViewModel) {
    TrackedThing.Type
        .entries
        .drop(1) // None is dropped
        .forEach { type ->
            DropdownMenuItem(
                text = { Text(type.name) },
                onClick = {
                    viewModel.showCreateDialog(type)
                })
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogComposable(
    viewModel: TrackerViewModel,
    defaultName: String,
    navigator: DestinationsNavigator
) {
    BasicAlertDialog(
        onDismissRequest = { viewModel.alertDialog.dismiss() },
        properties = DialogProperties()
    ) {
        Card {
            when (viewModel.dialogType) {
                TrackerViewModel.DialogType.Create,
                TrackerViewModel.DialogType.Edit ->
                    EditDialog(viewModel, defaultName)

                TrackerViewModel.DialogType.ConfirmDeletion,
                TrackerViewModel.DialogType.RefreshAll,
                TrackerViewModel.DialogType.ConfirmSpellRemovalFromList ->
                    ConfirmDialog(viewModel)

                TrackerViewModel.DialogType.AddPercentage,
                TrackerViewModel.DialogType.ReducePercentage ->
                    ValueInputDialog(viewModel, TrackedThing.Type.Percentage)

                TrackerViewModel.DialogType.AddNumber,
                TrackerViewModel.DialogType.ReduceNumber ->
                    ValueInputDialog(viewModel, TrackedThing.Type.Number)

                TrackerViewModel.DialogType.HealHealth,
                TrackerViewModel.DialogType.DamageHealth,
                TrackerViewModel.DialogType.AddTemporaryHp ->
                    ValueInputDialog(viewModel, TrackedThing.Type.Health)

                TrackerViewModel.DialogType.ShowSpellList ->
                    SpellListDialog(viewModel, navigator)

                TrackerViewModel.DialogType.SelectSlotLevelToCastSpell ->
                    SpellSlotSelectDialog(viewModel)

                TrackerViewModel.DialogType.EditText ->
                    ValueInputDialog(viewModel, TrackedThing.Type.Text)

                TrackerViewModel.DialogType.None -> Unit
            }
        }
    }
}

@Composable
private fun EditDialog(
    viewModel: TrackerViewModel,
    defaultName: String
) {
    DialogBase(viewModel) {
        val type by viewModel.editedTrackedThingType.collectAsState()
        NameTextField(viewModel, autoFocus = true, defaultValue = defaultName)
        SpellSlotLevelTextField(type, viewModel)
        ValueTextField(viewModel, type) { viewModel.setValue(it) }
    }
}

@Composable
private fun ConfirmDialog(viewModel: TrackerViewModel) {
    Column(
        Modifier.padding(16.dp),
        Arrangement.spacedBy(16.dp)
    ) {
        DialogTitle(viewModel.alertDialog.titleResource)
        Row(horizontalArrangement = Arrangement.End) {
            Spacer(Modifier.weight(1f))
            CancelButton(viewModel)
            TextButton(
                onClick = { viewModel.confirmDialogAction() },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    }
}

@Composable
private fun CancelButton(viewModel: TrackerViewModel) {
    TextButton(
        onClick = { viewModel.alertDialog.dismiss() },
        modifier = Modifier.wrapContentWidth()
    ) {
        Text(stringResource(R.string.cancel))
    }
}

@Composable
private fun ValueInputDialog(
    viewModel: TrackerViewModel,
    type: TrackedThing.Type
) {
    DialogBase(viewModel) {
        ValueTextField(
            viewModel,
            type,
            autoFocus = true
        ) { viewModel.updateValueInputField(it) }
    }
}

@Composable
private fun DialogBase(
    viewModel: TrackerViewModel,
    inputFields: @Composable () -> Unit
) {
    Column(
        Modifier.padding(16.dp),
        Arrangement.spacedBy(16.dp)
    ) {
        DialogTitle(viewModel.alertDialog.titleResource)
        inputFields()
        val buttonEnabled by viewModel.confirmButtonEnabled.collectAsState()
        TextButton(
            onClick = { viewModel.confirmDialogAction() },
            enabled = buttonEnabled,
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.End)
        ) {
            Text(stringResource(R.string.confirm))
        }
    }
}

@Composable
fun SpellListDialog(
    viewModel: TrackerViewModel,
    navigator: DestinationsNavigator
) {
    val spellListBeingPreviewed by viewModel.spellListBeingPreviewed.collectAsState()
    val dereferencedSpellList = spellListBeingPreviewed ?: return
    Column(
        Modifier.padding(16.dp),
        Arrangement.spacedBy(16.dp)
    ) {
        DialogTitle(viewModel.alertDialog.titleResource)
        LazyColumn(Modifier.heightIn(0.dp, 500.dp)) {
            items(
                dereferencedSpellList.spells,
                key = { getUniqueListItemKey(it) }) { spell ->
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    com.feko.generictabletoprpg.common.composable.OverviewListItem(
                        spell,
                        Modifier
                            .weight(1f)
                            .clickable {
                                navigator.navigate(SpellDetailsScreenDestination(spell.id))
                            },
                        ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (spell.level != 0) {
                            IconButton(
                                onClick = {
                                    viewModel.castSpellRequested(spell.level)
                                },
                                enabled = viewModel.canCastSpell(spell.level)
                            ) {
                                Icon(painterResource(R.drawable.celebration), "")
                            }
                        }
                        IconButton(
                            onClick = {
                                viewModel.removeSpellFromSpellListRequested(spell)
                            }
                        ) {
                            Icon(Icons.Default.Delete, "")
                        }
                    }
                }
            }
        }
        TextButton(
            onClick = { viewModel.alertDialog.dismiss() },
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.End)
        ) {
            Text(stringResource(R.string.dismiss))
        }
    }
}

@Composable
fun SpellSlotSelectDialog(viewModel: TrackerViewModel) {
    Column(
        Modifier.padding(16.dp),
        Arrangement.spacedBy(16.dp)
    ) {
        DialogTitle(viewModel.alertDialog.titleResource)
        val spellSlotLevels = requireNotNull(viewModel.availableSpellSlotsForSpellBeingCast)
        spellSlotLevels.forEach { spellSlotLevel ->
            ListItem(
                headlineContent = {
                    Text("${stringResource(R.string.level)} $spellSlotLevel")
                },
                modifier = Modifier.clickable {
                    viewModel.castSpell(spellSlotLevel)
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            Arrangement.End
        ) {
            CancelButton(viewModel)
        }
    }
}

@Composable
private fun NameTextField(
    viewModel: TrackerViewModel,
    @Suppress("SameParameterValue")
    autoFocus: Boolean = false,
    defaultValue: String
) {
    val focusRequester = remember { FocusRequester() }
    val nameInputData by viewModel.editedTrackedThingName.collectAsState()
    val focusManager = LocalFocusManager.current
    TextField(
        value = nameInputData.value,
        onValueChange = { viewModel.setName(it) },
        isError = !nameInputData.isValid,
        label = {
            Text(
                "${stringResource(R.string.name)} ($defaultValue)",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = { viewModel.setName("") }
            ) {
                Icon(Icons.Default.Clear, "")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )
    )
    if (autoFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
private fun SpellSlotLevelTextField(
    type: TrackedThing.Type,
    viewModel: TrackerViewModel
) {
    val focusManager = LocalFocusManager.current
    if (type == TrackedThing.Type.SpellSlot) {
        val spellSlotLevelInputData
                by viewModel.editedTrackedThingSpellSlotLevel.collectAsState()
        TextField(
            value = spellSlotLevelInputData.value,
            onValueChange = { viewModel.setLevel(it) },
            isError = !spellSlotLevelInputData.isValid,
            label = {
                Text(
                    stringResource(R.string.level),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { viewModel.setLevel("") }
                ) {
                    Icon(Icons.Default.Clear, "")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
    }
}

@Composable
private fun ValueTextField(
    viewModel: TrackerViewModel,
    type: TrackedThing.Type,
    autoFocus: Boolean = false,
    updateValue: (String) -> Unit
) {
    if (type == TrackedThing.Type.SpellList) {
        return
    }
    val focusRequester = remember { FocusRequester() }
    val valueInputData by viewModel.editedTrackedThingValue.collectAsState()
    TextField(
        value = valueInputData.value,
        onValueChange = { updateValue(it) },
        maxLines = if (type == TrackedThing.Type.Text) 5 else 1,
        isError = !valueInputData.isValid,
        suffix = {
            if (type == TrackedThing.Type.Percentage) {
                Text("%")
            }
        },
        label = {
            val label = if (type == TrackedThing.Type.Text) {
                stringResource(id = R.string.text)
            } else {
                stringResource(R.string.amount)
            }
            Text(
                label,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = { updateValue("") }
            ) {
                Icon(Icons.Default.Clear, "")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { viewModel.confirmDialogAction() }
        )
    )
    if (autoFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}