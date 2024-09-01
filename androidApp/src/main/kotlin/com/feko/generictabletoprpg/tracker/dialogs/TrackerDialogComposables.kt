package com.feko.generictabletoprpg.tracker.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.destinations.SpellDetailsScreenDestination
import com.feko.generictabletoprpg.searchall.getUniqueListItemKey
import com.feko.generictabletoprpg.tracker.CancelButton
import com.feko.generictabletoprpg.tracker.TrackedThing
import com.feko.generictabletoprpg.tracker.dialogs.IAlertDialogTrackerViewModel.DialogType
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

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
