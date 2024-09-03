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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.alertdialog.EmptyAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.composable.DialogTitle
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
import com.feko.generictabletoprpg.tracker.dialogs.IAlertDialogTrackerViewModel.DialogType
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
    val numberOfPreparedSpells = dereferencedSpellList.spells.count { it.isPrepared }
    Column(
        Modifier.padding(16.dp)
    ) {
        DialogTitle(viewModel.alertDialog.titleResource)
        Row(
            Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                buildString {
                    append(stringResource(R.string.known))
                    append(": ")
                    append(dereferencedSpellList.spells.size)
                    append(", ")
                    append(stringResource(R.string.prepared))
                    append(": ")
                    append(numberOfPreparedSpells)
                },
                Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = Typography.bodySmall
            )
            var isFilteringByPrepared by remember { mutableStateOf(false) }
            ElevatedFilterChip(
                isFilteringByPrepared,
                onClick = {
                    isFilteringByPrepared = !isFilteringByPrepared
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
        }
        LazyColumn(
            Modifier.heightIn(0.dp, 600.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                dereferencedSpellList.spells,
                key = { getUniqueListItemKey(it.toSpell()) }) { spellListEntry ->
                SpellListEntryListItem(spellListEntry, navigator, viewModel)
            }
        }
        TextButton(
            onClick = { viewModel.alertDialog.dismiss() },
            modifier = Modifier
                .wrapContentWidth()
                .align(Alignment.End)
                .padding(top = 8.dp)
        ) {
            Text(stringResource(R.string.dismiss))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
                        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                            Checkbox(
                                spellListEntry.isPrepared,
                                onCheckedChange = {

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
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
    viewModel: ISpellSlotLevelTextFieldTrackerViewModel
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
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
    viewModel: IValueTextFieldTrackerViewModel,
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
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                    override val spellListBeingPreviewed: StateFlow<SpellList?>
                        get() = MutableStateFlow(
                            SpellList(0, "Spell List", "", 0, 0)
                                .apply {
                                    spells = mutableListOf(
                                        getSpellListEntry1(),
                                        getSpellListEntry2()
                                    )
                                }
                        )

                    override fun removeSpellFromSpellListRequested(spell: SpellListEntry) = Unit

                    override fun castSpellRequested(level: Int) = Unit

                    override fun canCastSpell(level: Int): Boolean = true

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
        getSpellListEntry1(),
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
