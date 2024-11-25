package com.feko.generictabletoprpg.tracker.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.alertdialog.EmptyAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.common.composable.InputField
import com.feko.generictabletoprpg.destinations.SimpleSpellDetailsScreenDestination
import com.feko.generictabletoprpg.searchall.getUniqueListItemKey
import com.feko.generictabletoprpg.spell.Spell
import com.feko.generictabletoprpg.spell.SpellRange
import com.feko.generictabletoprpg.theme.Typography
import com.feko.generictabletoprpg.tracker.CancelButton
import com.feko.generictabletoprpg.tracker.EmptyTrackerViewModel
import com.feko.generictabletoprpg.tracker.SpellList
import com.feko.generictabletoprpg.tracker.SpellListEntry
import com.feko.generictabletoprpg.tracker.TrackedThing
import com.feko.generictabletoprpg.tracker.cantripSpellsCount
import com.feko.generictabletoprpg.tracker.containsPreparedAndCantripSpells
import com.feko.generictabletoprpg.tracker.dialogs.IAlertDialogTrackerViewModel.DialogType
import com.feko.generictabletoprpg.tracker.filterPreparedAndCantrips
import com.feko.generictabletoprpg.tracker.preparedSpellsCount
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogComposable(
    viewModel: IAlertDialogTrackerViewModel,
    defaultName: String,
    navigator: DestinationsNavigator
) {
    BasicAlertDialog(
        onDismissRequest = { viewModel.alertDialog.dismiss() },
        properties = DialogProperties()
    ) {
        Card {
            when (viewModel.dialogType) {
                DialogType.Create,
                DialogType.Edit ->
                    EditDialog(viewModel, defaultName)

                DialogType.ConfirmDeletion,
                DialogType.RefreshAll,
                DialogType.ConfirmSpellRemovalFromList ->
                    ConfirmDialog(viewModel)

                DialogType.AddPercentage,
                DialogType.ReducePercentage ->
                    ValueInputDialog(viewModel, TrackedThing.Type.Percentage)

                DialogType.AddNumber,
                DialogType.ReduceNumber ->
                    ValueInputDialog(viewModel, TrackedThing.Type.Number)

                DialogType.HealHealth,
                DialogType.DamageHealth,
                DialogType.AddTemporaryHp ->
                    ValueInputDialog(viewModel, TrackedThing.Type.Health)

                DialogType.ShowSpellList ->
                    SpellListDialog(viewModel, navigator)

                DialogType.SelectSlotLevelToCastSpell ->
                    SpellSlotSelectDialog(viewModel)

                DialogType.EditText ->
                    ValueInputDialog(viewModel, TrackedThing.Type.Text)

                DialogType.None -> Unit
            }
        }
    }
}

@Composable
private fun EditDialog(
    viewModel: IEditDialogTrackerViewModel,
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
private fun ConfirmDialog(viewModel: IConfirmDialogTrackerViewModel) {
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
private fun ValueInputDialog(
    viewModel: IValueInputDialogTrackerViewModel,
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
    viewModel: IBaseDialogTrackerViewModel,
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
    viewModel: ISpellListDialogTrackerViewModel,
    navigator: DestinationsNavigator
) {
    val spellListBeingPreviewed by viewModel.spellListBeingPreviewed.collectAsState()
    val dereferencedSpellList = spellListBeingPreviewed ?: return
    val numberOfPreparedSpells = dereferencedSpellList.serializedItem.preparedSpellsCount()
    val numberOfCantripSpells = dereferencedSpellList.serializedItem.cantripSpellsCount()
    Column(
        Modifier.padding(16.dp)
    ) {
        DialogTitle(viewModel.alertDialog.titleResource)
        val isFilteringByPrepared by viewModel.isShowingPreparedSpells.collectAsState()
        Row(
            Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                buildString {
                    append(stringResource(R.string.known))
                    append(": ")
                    append(dereferencedSpellList.serializedItem.size)
                    if (numberOfPreparedSpells > 0) {
                        append(", ")
                        append(stringResource(R.string.prepared))
                        append(": ")
                        append(numberOfPreparedSpells)
                    }
                    if (numberOfCantripSpells > 0) {
                        append(", ")
                        append(stringResource(R.string.cantrips))
                        append(": ")
                        append(numberOfCantripSpells)
                    }
                },
                Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = Typography.bodySmall
            )
            if (dereferencedSpellList.serializedItem.containsPreparedAndCantripSpells()) {
                ElevatedFilterChip(
                    isFilteringByPrepared,
                    onClick = {
                        viewModel.setShowingPreparedSpells(!isFilteringByPrepared)
                    },
                    label = {
                        Text(stringResource(R.string.prepared))
                    },
                    leadingIcon =
                    {
                        if (isFilteringByPrepared) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.check_box_outline_blank),
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    }
                )
            } else {
                viewModel.setShowingPreparedSpells(false)
            }
        }
        LazyColumn(
            Modifier.heightIn(0.dp, 600.dp),
            viewModel.spellListState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                dereferencedSpellList.serializedItem.filterPreparedAndCantrips(isFilteringByPrepared),
                key = { getUniqueListItemKey(it.toSpell()) }) { spellListEntry ->
                SpellListEntryListItem(spellListEntry, navigator, viewModel)
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            Arrangement.SpaceBetween
        ) {
            Row {
                val coroutineScope = rememberCoroutineScope()
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.spellListState.animateScrollToItem(0)
                        }
                    },
                    enabled = viewModel.spellListState.canScrollBackward
                ) { Icon(painterResource(R.drawable.vertical_align_top), "") }
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.spellListState.animateScrollToItem(dereferencedSpellList.serializedItem.size - 1)
                        }
                    },
                    enabled = viewModel.spellListState.canScrollForward
                ) { Icon(painterResource(R.drawable.vertical_align_bottom), "") }
            }
            TextButton(
                onClick = { viewModel.alertDialog.dismiss() },
                modifier = Modifier.wrapContentWidth()
            ) { Text(stringResource(R.string.dismiss)) }
        }
    }
}

@Composable
private fun SpellListEntryListItem(
    spellListEntry: SpellListEntry,
    navigator: DestinationsNavigator,
    viewModel: ISpellListDialogTrackerViewModel
) {
    ElevatedCard(onClick = {
        navigator.navigate(
            SimpleSpellDetailsScreenDestination.invoke(
                spellListEntry.toSpell()
            )
        )
    }) {
        Column {
            Row(
                Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    spellListEntry.name,
                    Modifier
                        .weight(1f)
                        .padding(vertical = 16.dp),
                    style = Typography.titleMedium
                )
                if (spellListEntry.level > 0) {
                    Column(
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                            Checkbox(
                                spellListEntry.isPrepared,
                                onCheckedChange = {
                                    viewModel.changeSpellListEntryPreparedState(spellListEntry, it)
                                }
                            )
                        }
                        Text(
                            stringResource(R.string.prepared),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            style = Typography.labelSmall
                        )
                    }
                }
            }
            HorizontalDivider(Modifier.padding(horizontal = 2.dp))
            Text(
                "${stringResource(R.string.level)} ${spellListEntry.level}, ${spellListEntry.school}",
                Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = Typography.bodySmall
            )
            Text(
                "${stringResource(R.string.casting_time)}: ${spellListEntry.castingTime}",
                Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = Typography.bodySmall
            )
            HorizontalDivider(Modifier.padding(horizontal = 2.dp))
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceAround,
                Alignment.CenterVertically
            ) {
                if (spellListEntry.level != 0) {
                    TextButton(
                        onClick = {
                            viewModel.castSpellRequested(spellListEntry.level)
                        },
                        enabled = viewModel.canCastSpell(spellListEntry.level)
                    ) {
                        Text(stringResource(R.string.cast))
                    }
                }
                TextButton(
                    onClick = {
                        viewModel.removeSpellFromSpellListRequested(spellListEntry)
                    }
                ) {
                    Text(stringResource(R.string.remove))
                }
            }
        }
    }
}

@Composable
fun SpellSlotSelectDialog(viewModel: ISpellSlotSelectDialogTrackerViewModel) {
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
    viewModel: INameTextFieldTrackerViewModel,
    @Suppress("SameParameterValue")
    autoFocus: Boolean = false,
    defaultValue: String
) {
    val nameInputData by viewModel.editedTrackedThingName.collectAsState()
    InputField(
        nameInputData.value,
        "${stringResource(R.string.name)} ($defaultValue)",
        onValueChange = { viewModel.setName(it) },
        isInputFieldValid = { nameInputData.isValid },
        autoFocus = autoFocus
    )
}

@Composable
private fun SpellSlotLevelTextField(
    type: TrackedThing.Type,
    viewModel: ISpellSlotLevelTextFieldTrackerViewModel
) {
    if (type != TrackedThing.Type.SpellSlot) return

    val spellSlotLevelInputData
            by viewModel.editedTrackedThingSpellSlotLevel.collectAsState()
    InputField(
        value = spellSlotLevelInputData.value,
        label = stringResource(R.string.level),
        onValueChange = { viewModel.setLevel(it) },
        isInputFieldValid = { spellSlotLevelInputData.isValid },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
private fun ValueTextField(
    viewModel: IValueTextFieldTrackerViewModel,
    type: TrackedThing.Type,
    autoFocus: Boolean = false,
    updateValue: (String) -> Unit
) {
    if (type == TrackedThing.Type.SpellList) {
        return
    }
    val valueInputData by viewModel.editedTrackedThingValue.collectAsState()
    InputField(
        value = valueInputData.value,
        label = if (type == TrackedThing.Type.Text) {
            stringResource(id = R.string.text)
        } else {
            stringResource(R.string.amount)
        },
        onValueChange = updateValue,
        onFormSubmit = { viewModel.confirmDialogAction() },
        isInputFieldValid = { valueInputData.isValid },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        autoFocus = autoFocus,
        maxLines = if (type == TrackedThing.Type.Text) 5 else 1,
        suffix = {
            if (type == TrackedThing.Type.Percentage) {
                Text("%")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SpellListDialogPreview() {
    BasicAlertDialog(
        onDismissRequest = {},
        properties = DialogProperties()
    ) {
        Card {
            SpellListDialog(
                object : ISpellListDialogTrackerViewModel {
                    override val spellListState: LazyListState
                        get() = LazyListState()
                    override val isShowingPreparedSpells: MutableStateFlow<Boolean>
                        get() = MutableStateFlow(false)

                    override fun setShowingPreparedSpells(value: Boolean) = Unit

                    override val spellListBeingPreviewed: StateFlow<SpellList?>
                        get() = MutableStateFlow(
                            SpellList(0, "Spell List", "", 0, 0)
                                .apply {
                                    serializedItem = mutableListOf(
                                        getSpellListEntry1(),
                                        getSpellListEntry2()
                                    )
                                }
                        )

                    override fun removeSpellFromSpellListRequested(spell: SpellListEntry) = Unit

                    override fun castSpellRequested(level: Int) = Unit

                    override fun canCastSpell(level: Int): Boolean = true

                    override fun changeSpellListEntryPreparedState(
                        spellListEntry: SpellListEntry,
                        isPrepared: Boolean
                    ) = Unit

                    override val alertDialog: IAlertDialogSubViewModel
                        get() = EmptyAlertDialogSubViewModel

                },

                EmptyDestinationsNavigator
            )
        }
    }
}

@Composable
@Preview
fun SpellListEntryListItemPreview() {
    SpellListEntryListItem(
        getSpellListEntry2(),
        EmptyDestinationsNavigator,
        EmptyTrackerViewModel
    )
}

private fun getSpellListEntry1() = SpellListEntry(
    id = 1,
    name = "Spell 1",
    description = "",
    school = "Abjuration",
    duration = "",
    concentration = true,
    level = 0,
    source = "",
    components = Spell.SpellComponents(
        verbal = true,
        somatic = true,
        material = true,
        materialComponent = null
    ),
    castingTime = "1 action",
    classesThatCanCast = listOf(),
    range = SpellRange(
        isSelf = true,
        isTouch = true,
        isSight = true,
        distance = 0,
        unit = null
    ),
    isRitual = true,
    isPrepared = false
)

private fun getSpellListEntry2() = SpellListEntry(
    id = 2,
    name = "Spell 2",
    description = "",
    school = "Conjuration",
    duration = "",
    concentration = true,
    level = 1,
    source = "",
    components = Spell.SpellComponents(
        true,
        somatic = true,
        material = true,
        materialComponent = null
    ),
    castingTime = "1 bonus action",
    classesThatCanCast = listOf(),
    range = SpellRange(
        isSelf = true,
        isTouch = true,
        isSight = true,
        distance = 0,
        unit = null
    ),
    isRitual = true,
    isPrepared = true
)
