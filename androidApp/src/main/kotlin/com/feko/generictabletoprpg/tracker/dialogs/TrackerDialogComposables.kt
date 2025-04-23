package com.feko.generictabletoprpg.tracker.dialogs


import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.asSignedString
import com.feko.generictabletoprpg.common.composable.AlertDialogBase
import com.feko.generictabletoprpg.common.composable.BoxWithScrollIndicator
import com.feko.generictabletoprpg.common.composable.CheckboxWithText
import com.feko.generictabletoprpg.common.composable.ConfirmationDialog
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.common.composable.EnterValueDialog
import com.feko.generictabletoprpg.common.composable.IInputFieldValueConverter
import com.feko.generictabletoprpg.common.composable.InputField
import com.feko.generictabletoprpg.common.composable.NumberInputField
import com.feko.generictabletoprpg.common.composable.SelectFromListDialog
import com.feko.generictabletoprpg.searchall.getUniqueListItemKey
import com.feko.generictabletoprpg.spell.Spell
import com.feko.generictabletoprpg.spell.SpellRange
import com.feko.generictabletoprpg.theme.Typography
import com.feko.generictabletoprpg.tracker.IntTrackedThing
import com.feko.generictabletoprpg.tracker.Percentage
import com.feko.generictabletoprpg.tracker.SpellList
import com.feko.generictabletoprpg.tracker.SpellListEntry
import com.feko.generictabletoprpg.tracker.SpellSlot
import com.feko.generictabletoprpg.tracker.StatEntry
import com.feko.generictabletoprpg.tracker.StatSkillEntry
import com.feko.generictabletoprpg.tracker.Stats
import com.feko.generictabletoprpg.tracker.StatsContainer
import com.feko.generictabletoprpg.tracker.Text
import com.feko.generictabletoprpg.tracker.TrackedThing
import com.feko.generictabletoprpg.tracker.TrackerViewModel
import com.feko.generictabletoprpg.tracker.cantripSpellsCount
import com.feko.generictabletoprpg.tracker.filterPreparedAndCantrips
import com.feko.generictabletoprpg.tracker.preparedSpellsCount
import com.ramcosta.composedestinations.generated.destinations.SimpleSpellDetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch

@Composable
fun TrackerAlertDialogs(viewModel: TrackerViewModel, navigator: DestinationsNavigator) {
    SpellListDialogWithViewModel(viewModel, navigator)
    ConfirmDeletionDialog(viewModel)
    RefreshAllDialog(viewModel)
    ConfirmSpellRemovalFromListDialog(viewModel)
    AddToPercentageDialog(viewModel)
    SubtractFromPercentageDialog(viewModel)
    AddToNumberDialog(viewModel)
    SubtractFromNumberDialog(viewModel)
    DamageHealthDialog(viewModel)
    HealHealthDialog(viewModel)
    AddTemporaryHpDialog(viewModel)
    SelectSpellSlotLevelToCastDialog(viewModel)
    PreviewStatSkillsDialog(viewModel)
    EditDialog(viewModel)
    StatsEditDialog(
        viewModel.statsEditDialog,
        viewModel.groupName
    ) {
        viewModel.insertOrUpdateStats(it)
    }
}

@Composable
fun ConfirmDeletionDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.confirmDeletionDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    ConfirmationDialog(
        onConfirm = { viewModel.deleteTrackedThing(viewModel.confirmDeletionDialog.state.value) },
        onDialogDismissed = { viewModel.confirmDeletionDialog.dismiss() }
    )
}

@Composable
fun RefreshAllDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.refreshAllDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    ConfirmationDialog(
        onConfirm = { viewModel.refreshAll() },
        onDialogDismissed = { viewModel.refreshAllDialog.dismiss() },
        dialogTitle = stringResource(R.string.refresh_all_tracked_things_dialog_title)
    )
}

@Composable
fun ConfirmSpellRemovalFromListDialog(viewModel: TrackerViewModel) {
    val isDialogVisible
            by viewModel.confirmSpellRemovalFromListDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    ConfirmationDialog(
        onConfirm = {
            viewModel.removeSpellFromSpellList(
                viewModel.confirmSpellRemovalFromListDialog.state.value
            )
        },
        onDialogDismissed = { viewModel.confirmSpellRemovalFromListDialog.dismiss() },
        dialogTitle = stringResource(R.string.confirm_spell_removal_from_list_dialog_title)
    )
}

@Composable
fun AddToPercentageDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.addPercentageDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    EnterValueDialog(
        onConfirm = { viewModel.addToPercentage(viewModel.addPercentageDialog.state.value, it) },
        onDialogDismissed = { viewModel.addPercentageDialog.dismiss() },
        dialogTitle = R.string.increase_percentage_dialog_title,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        suffix = { Text("%") }
    )
}

@Composable
fun SubtractFromPercentageDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.reducePercentageDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    EnterValueDialog(
        onConfirm = {
            viewModel.subtractFromPercentage(viewModel.reducePercentageDialog.state.value, it)
        },
        onDialogDismissed = { viewModel.reducePercentageDialog.dismiss() },
        dialogTitle = R.string.reduce_percentage_dialog_title,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        suffix = { Text("%") }
    )
}

@Composable
fun AddToNumberDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.addNumberDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    EnterValueDialog(
        onConfirm = {
            viewModel.addToNumber(viewModel.addNumberDialog.state.value, it)
        },
        onDialogDismissed = { viewModel.addNumberDialog.dismiss() },
        dialogTitle = R.string.add,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun SubtractFromNumberDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.reduceNumberDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    EnterValueDialog(
        onConfirm = {
            viewModel.subtractFromNumber(viewModel.reduceNumberDialog.state.value, it)
        },
        onDialogDismissed = { viewModel.reduceNumberDialog.dismiss() },
        dialogTitle = R.string.subtract,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun HealHealthDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.healHealthDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    EnterValueDialog(
        onConfirm = {
            viewModel.healHealth(viewModel.healHealthDialog.state.value, it)
        },
        onDialogDismissed = { viewModel.healHealthDialog.dismiss() },
        dialogTitle = viewModel.healHealthDialog.titleResource,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun DamageHealthDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.damageHealthDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    EnterValueDialog(
        onConfirm = {
            viewModel.damageHealth(viewModel.damageHealthDialog.state.value, it)
        },
        onDialogDismissed = { viewModel.damageHealthDialog.dismiss() },
        dialogTitle = viewModel.damageHealthDialog.titleResource,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun AddTemporaryHpDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.addTemporaryHpDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    EnterValueDialog(
        onConfirm = {
            viewModel.addTemporaryHpToTrackedThing(viewModel.addTemporaryHpDialog.state.value, it)
        },
        onDialogDismissed = { viewModel.addTemporaryHpDialog.dismiss() },
        dialogTitle = viewModel.addTemporaryHpDialog.titleResource,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun SelectSpellSlotLevelToCastDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.selectSlotLevelToCastDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    val listItems by viewModel.selectSlotLevelToCastDialog.state.collectAsState(emptyList())
    SelectFromListDialog(
        viewModel.selectSlotLevelToCastDialog.titleResource,
        listItems,
        getListItemKey = { it },
        onItemSelected = { viewModel.castSpell(it) },
        onDialogDismissed = { viewModel.selectSlotLevelToCastDialog.dismiss() }
    ) {
        ListItem(headlineContent = { Text("${stringResource(R.string.level)} $it") })
    }
}

@Composable
fun PreviewStatSkillsDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.showStatsDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    AlertDialogBase(
        onDialogDismissed = { viewModel.showStatsDialog.dismiss() },
        screenHeight = 0.7f,
        dialogTitle = { DialogTitle(stringResource(viewModel.showStatsDialog.titleResource)) },
        Arrangement.Top,
        dialogButtons = {
            TextButton(
                onClick = { viewModel.showStatsDialog.dismiss() },
                modifier = Modifier.wrapContentWidth()
            ) { Text(stringResource(R.string.dismiss)) }
        }
    ) {
        val statsContainer by viewModel.showStatsDialog.state.collectAsState()
        val stats = statsContainer.stats
        val skills = stats
            .flatMap { it.skills }
            .sortedBy { it.name }
        val passiveSkills = skills.filter { it.showPassive }
        val scrollState = rememberScrollState()
        BoxWithScrollIndicator(
            scrollState,
            CardDefaults.cardColors().containerColor,
            Modifier.weight(1f),
        ) {
            Column(Modifier.verticalScroll(scrollState)) {
                HeaderWithDividers(stringResource(R.string.passive_skills))
                passiveSkills.forEachIndexed { index, passiveSkill ->
                    SkillRow(passiveSkill.name, passiveSkill.passiveScore.toString())
                    if (index < passiveSkills.size - 1) {
                        HorizontalDivider(Modifier.padding(horizontal = 80.dp, vertical = 4.dp))
                    }
                }
                HeaderWithDividers(stringResource(R.string.saving_throws))
                stats.forEachIndexed { index, stat ->
                    SkillRow(stat.name, stat.savingThrowBonus.asSignedString())
                    if (index < stats.size - 1) {
                        HorizontalDivider(Modifier.padding(horizontal = 80.dp, vertical = 4.dp))
                    }
                }
                HeaderWithDividers(stringResource(R.string.skill_bonuses))
                skills.forEachIndexed { index, skill ->
                    SkillRow(skill.name, skill.bonus.asSignedString())
                    if (index < skills.size - 1) {
                        HorizontalDivider(Modifier.padding(horizontal = 80.dp, vertical = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun EditDialog(
    viewModel: TrackerViewModel,
    defaultName: String = viewModel.groupName
) {
    val isDialogVisible by viewModel.editDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    val editedTrackedThing by viewModel.editDialog.state.collectAsState()
    val canConfirmEditOperation = editedTrackedThing.validate()
    AlertDialogBase(
        onDialogDismissed = { viewModel.editDialog.dismiss() },
        dialogTitle = { DialogTitle(stringResource(viewModel.editDialog.titleResource)) },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        dialogButtons = {
            TextButton(onClick = { viewModel.editDialog.dismiss() }) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(
                onClick = { confirmEditOperation(viewModel, editedTrackedThing) },
                enabled = canConfirmEditOperation,
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    ) {
        val isSpellList = editedTrackedThing is SpellList
        val onFormSubmit = {
            if (canConfirmEditOperation) {
                confirmEditOperation(viewModel, editedTrackedThing)
            }
        }
        InputField(
            editedTrackedThing.name,
            "${stringResource(R.string.name)} ($defaultName)",
            onValueChange = {
                viewModel.editDialog.updateState(editedTrackedThing.copy().apply { name = it })
            },
            onFormSubmit = onFormSubmit,
            keyboardOptions = KeyboardOptions(
                imeAction = if (isSpellList) ImeAction.Done else ImeAction.Next
            ),
            autoFocus = true
        )
        val spellSlot = editedTrackedThing as? SpellSlot
        if (spellSlot != null) {
            NumberInputField(
                value = spellSlot.level,
                label = stringResource(R.string.level),
                convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                onValueChange = {
                    viewModel.editDialog.updateState(spellSlot.apply { level = it }.copy())
                },
                isInputFieldValid = { spellSlot.isLevelValid() },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
        }
        EditDialogValueInputField(
            isSpellList,
            editedTrackedThing,
            {
                viewModel.editDialog.updateState(
                    editedTrackedThing
                        .copy()
                        .apply {
                            setNewValue(it)
                            defaultValue = it
                        }
                )
            },
            onFormSubmit
        )
    }
}

private fun confirmEditOperation(
    viewModel: TrackerViewModel,
    editedTrackedThing: TrackedThing
) {
    viewModel.createOrEditTrackedThing(editedTrackedThing)
    viewModel.editDialog.dismiss()
}

@Composable
private fun EditDialogValueInputField(
    isSpellList: Boolean,
    editedTrackedThing: TrackedThing,
    onValueChange: (String) -> Unit,
    onFormSubmit: () -> Unit
) {
    if (isSpellList) return

    if (editedTrackedThing is Text) {
        InputField(
            value = editedTrackedThing.value,
            label = stringResource(id = R.string.text),
            onValueChange = onValueChange,
            onFormSubmit = onFormSubmit,
            isInputFieldValid = { editedTrackedThing.isValueValid() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            maxLines = 5
        )
    }

    val percentage = editedTrackedThing as? Percentage
    if (percentage != null) {
        NumberInputField(
            percentage.amount,
            label = stringResource(R.string.amount),
            convertInputValue = IInputFieldValueConverter.FloatInputFieldValueConverter,
            onValueChange = { onValueChange(it.toString()) },
            canSubmitForm = { percentage.isValueValid() },
            onFormSubmit = onFormSubmit,
            isInputFieldValid = { percentage.isValueValid() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            suffix = { Text("%") }
        )
    }

    val intTrackedThing = editedTrackedThing as? IntTrackedThing
    if (intTrackedThing != null) {
        NumberInputField(
            intTrackedThing.amount,
            label = stringResource(R.string.amount),
            convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
            onValueChange = { onValueChange(it.toString()) },
            canSubmitForm = { intTrackedThing.isValueValid() },
            onFormSubmit = onFormSubmit,
            isInputFieldValid = { intTrackedThing.isValueValid() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
fun SpellListDialogWithViewModel(
    viewModel: TrackerViewModel,
    navigator: DestinationsNavigator
) {
    val isDialogVisible by viewModel.spellListDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    val spellList by viewModel.spellListDialog.state.collectAsState()
    val isFilteringByPrepared by viewModel.isShowingPreparedSpells.collectAsState()
    SpellListDialog(
        spellList,
        isFilteringByPrepared,
        navigator,
        viewModel.spellListDialog.titleResource,
        viewModel.spellListState,
        onDialogDismissed = { viewModel.spellListDialog.dismiss() },
        onFilteringByPreparedStateChanged = { viewModel.setShowingPreparedSpells(it) },
        canSpellBeCast = { level -> viewModel.canCastSpell(level) },
        onSpellPreparedStateChanged = { spellListEntry, isPrepared ->
            viewModel.changeSpellListEntryPreparedState(spellListEntry, isPrepared)
        },
        onCastSpellRequested = { level -> viewModel.castSpellRequested(level) },
        onRemoveSpellRequested = { viewModel.removeSpellFromSpellListRequested(it) }
    )
}

@Composable
fun SpellListDialog(
    spellList: SpellList,
    isFilteringByPrepared: Boolean,
    navigator: DestinationsNavigator,
    @StringRes
    dialogTitleResource: Int,
    spellListState: LazyListState,
    onDialogDismissed: () -> Unit,
    onFilteringByPreparedStateChanged: (Boolean) -> Unit,
    canSpellBeCast: (level: Int) -> Boolean,
    onSpellPreparedStateChanged: (SpellListEntry, Boolean) -> Unit,
    onCastSpellRequested: (level: Int) -> Unit,
    onRemoveSpellRequested: (spell: SpellListEntry) -> Unit
) {
    AlertDialogBase(
        onDialogDismissed,
        dialogTitle = { DialogTitle(stringResource(dialogTitleResource)) },
        dialogButtons = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                Arrangement.SpaceBetween
            ) {
                val coroutineScope = rememberCoroutineScope()
                Row {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                spellListState.animateScrollToItem(0)
                            }
                        },
                        enabled = spellListState.canScrollBackward
                    ) { Icon(painterResource(R.drawable.vertical_align_top), "") }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                spellListState.animateScrollToItem(spellList.serializedItem.size - 1)
                            }
                        },
                        enabled = spellListState.canScrollForward
                    ) { Icon(painterResource(R.drawable.vertical_align_bottom), "") }
                }
                TextButton(
                    onClick = onDialogDismissed,
                    modifier = Modifier.wrapContentWidth()
                ) { Text(stringResource(R.string.dismiss)) }
            }
        }
    ) {
        val numberOfPreparedSpells = spellList.serializedItem.preparedSpellsCount()
        val numberOfCantripSpells = spellList.serializedItem.cantripSpellsCount()
        Row(
            Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                buildString {
                    append(stringResource(R.string.known))
                    append(": ")
                    append(spellList.serializedItem.size)
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
            if (numberOfPreparedSpells > 0 || numberOfCantripSpells > 0) {
                ElevatedFilterChip(
                    isFilteringByPrepared,
                    onClick = {
                        onFilteringByPreparedStateChanged(!isFilteringByPrepared)
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
                onFilteringByPreparedStateChanged(false)
            }
        }
        LazyColumn(
            Modifier.heightIn(0.dp, (LocalConfiguration.current.screenHeightDp * 0.7f).dp),
            spellListState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                spellList.serializedItem.filterPreparedAndCantrips(isFilteringByPrepared),
                key = { getUniqueListItemKey(it.toSpell()) }) { spellListEntry ->
                SpellListEntryListItem(
                    spellListEntry,
                    canSpellBeCast(spellListEntry.level),
                    navigator,
                    onSpellPreparedStateChanged = {
                        onSpellPreparedStateChanged(spellListEntry, it)
                    },
                    onCastSpellClicked = { onCastSpellRequested(spellListEntry.level) },
                    onRemoveSpellRequested = { onRemoveSpellRequested(spellListEntry) },
                )
            }
        }
    }
}

@Composable
private fun SpellListEntryListItem(
    spellListEntry: SpellListEntry,
    canCastSpell: Boolean,
    navigator: DestinationsNavigator,
    onSpellPreparedStateChanged: (Boolean) -> Unit,
    onCastSpellClicked: () -> Unit,
    onRemoveSpellRequested: () -> Unit,
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
                                onCheckedChange = onSpellPreparedStateChanged
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
                        onClick = onCastSpellClicked,
                        enabled = canCastSpell
                    ) {
                        Text(stringResource(R.string.cast))
                    }
                }
                TextButton(onClick = onRemoveSpellRequested) {
                    Text(stringResource(R.string.remove))
                }
            }
        }
    }
}

@Composable
fun StatsEditDialog(
    viewModel: IStatsEditDialogSubViewModel,
    defaultName: String,
    onFormSubmitted: (Stats) -> Unit
) {
    val isDialogVisible by viewModel.alertDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    val editedStats by viewModel.alertDialog.state.collectAsState()
    val statsContainer = requireNotNull(editedStats.serializedItem)
    AlertDialogBase(
        onDialogDismissed = { viewModel.alertDialog.dismiss() },
        screenHeight = 0.6f,
        dialogTitle = { DialogTitle(stringResource(viewModel.alertDialog.titleResource)) },
        dialogButtons = {
            TextButton(
                onClick = {
                    onFormSubmitted(editedStats)
                    viewModel.alertDialog.dismiss()
                },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    ) {
        val scrollState = rememberScrollState()
        BoxWithScrollIndicator(
            scrollState,
            backgroundColor = CardDefaults.cardColors().containerColor,
            Modifier.weight(1f)
        ) {
            Column(Modifier.verticalScroll(scrollState)) {
                Text(
                    stringResource(R.string.additional_bonus_hint),
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = Typography.bodySmall
                )
                InputField(
                    value = editedStats.name,
                    label = "${stringResource(R.string.name)} ($defaultName)",
                    onValueChange = { viewModel.updateStatsName(it) },
                )
                NumberInputField(
                    value = statsContainer.proficiencyBonus,
                    label = stringResource(R.string.proficiency_bonus),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = {
                        viewModel.updateStatsProficiencyBonus(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                NumberInputField(
                    value = statsContainer.initiativeAdditionalBonus,
                    label = stringResource(R.string.initiative_additional_bonus),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = {
                        viewModel.updateStatsInitiativeAdditionalBonus(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                NumberInputField(
                    value = statsContainer.spellSaveDcAdditionalBonus,
                    label = stringResource(R.string.spell_save_dc_additional_bonus),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = {
                        viewModel.updateSpellSaveDcAdditionalBonus(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                NumberInputField(
                    value = statsContainer.spellAttackAdditionalBonus,
                    label = stringResource(R.string.spell_attack_additional_bonus),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = {
                        viewModel.updateSpellAttackAdditionalBonus(it)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                for ((statIndex, statEntry) in statsContainer.stats.withIndex()) {
                    Spacer(Modifier.height(8.dp))
                    StatsStatEntry(
                        statEntry,
                        statIndex,
                        viewModel,
                        statsContainer,
                        onFormSubmit = {
                            onFormSubmitted(editedStats)
                            viewModel.alertDialog.dismiss()
                        }
                    )
                    if (statIndex < statsContainer.stats.size - 1) {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SkillRow(
    skillName: String,
    skillValue: String
) {
    Row(
        Modifier.fillMaxWidth(),
        Arrangement.SpaceBetween
    ) {
        Text(skillName)
        Text(skillValue)
    }
}

@Composable
private fun StatsStatEntry(
    statEntry: StatEntry,
    statIndex: Int,
    viewModel: IStatsEditDialogSubViewModel,
    statsContainer: StatsContainer,
    onFormSubmit: () -> Unit
) {
    HeaderWithDividers("${statEntry.name} (${statEntry.shortName})")
    key("editStatEntry-$statIndex") {
        val bonusText = "(${statEntry.bonus.asSignedString()})"
        NumberInputField(
            value = statEntry.score,
            label = "${stringResource(R.string.score)}$bonusText",
            convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
            onValueChange = {
                viewModel.updateStatScore(statIndex, it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        NumberInputField(
            value = statEntry.savingThrowAdditionalBonus,
            label = stringResource(R.string.saving_throw_additional_bonus),
            convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
            onValueChange = {
                viewModel.updateStatSavingThrowAdditionalBonus(statIndex, it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
    }
    CheckboxWithText(
        statEntry.isProficientInSavingThrow,
        R.string.saving_throw_proficiency
    ) { checked -> viewModel.updateStatSavingThrowProficiency(statIndex, checked) }
    CheckboxWithText(
        statEntry.isSpellcastingModifier,
        R.string.spellcasting_modifier
    ) { checked -> viewModel.updateStatSpellcastingModifier(statIndex, checked) }
    if (statEntry.skills.isNotEmpty()) {
        Column(
            Modifier
                .background(
                    CardDefaults.elevatedCardColors().containerColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            for ((skillIndex, skill) in statEntry.skills.withIndex()) {
                StatsStatEntrySkill(
                    skill,
                    statIndex,
                    skillIndex,
                    statsContainer,
                    statEntry,
                    viewModel,
                    onFormSubmit
                )
            }
        }
    }
}

@Composable
private fun StatsStatEntrySkill(
    skill: StatSkillEntry,
    statIndex: Int,
    skillIndex: Int,
    statsContainer: StatsContainer,
    statEntry: StatEntry,
    viewModel: IStatsEditDialogSubViewModel,
    onFormSubmit: () -> Unit
) {
    HeaderWithDividers(skill.name, Modifier.padding(bottom = 8.dp))
    key("editStatEntry-$statIndex-$skillIndex") {
        val imeAction =
            if (statIndex == statsContainer.stats.size - 1
                && skillIndex == statEntry.skills.size - 1
            ) {
                ImeAction.Done
            } else {
                ImeAction.Next
            }
        NumberInputField(
            value = skill.additionalBonus,
            label = stringResource(R.string.skill_additional_bonus),
            convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
            onValueChange = {
                viewModel.updateStatSkillAdditionalBonus(statIndex, skillIndex, it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeAction
            ),
            onFormSubmit = onFormSubmit,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            )
        )
    }
    CheckboxWithText(
        skill.isProficient,
        R.string.proficiency
    ) { checked -> viewModel.updateStatSkillProficiency(statIndex, skillIndex, checked) }
}

@Composable
private fun HeaderWithDividers(
    headerText: String,
    modifier: Modifier = Modifier
) {
    Row(
        Modifier
            .padding(vertical = 8.dp)
            .then(modifier),
        Arrangement.spacedBy(8.dp),
        Alignment.CenterVertically
    ) {
        HorizontalDivider(Modifier.weight(1f))
        Text(headerText)
        HorizontalDivider(Modifier.weight(1f))
    }
}

@Preview
@Composable
fun SpellListDialogPreview() {
    SpellListDialog(
        spellList = SpellList(0, "Spell List", "", 0, 0)
            .apply {
                serializedItem = mutableListOf(
                    getSpellListEntry1(),
                    getSpellListEntry2()
                )
            },
        isFilteringByPrepared = false,
        navigator = EmptyDestinationsNavigator,
        dialogTitleResource = R.string.spell_list,
        spellListState = rememberLazyListState(),
        onDialogDismissed = {},
        onFilteringByPreparedStateChanged = {},
        canSpellBeCast = { true },
        onSpellPreparedStateChanged = { _, _ -> },
        onCastSpellRequested = {},
        onRemoveSpellRequested = {}
    )
}

@Composable
@Preview
fun SpellListEntryListItemPreview() {
    SpellListEntryListItem(
        getSpellListEntry2(),
        canCastSpell = true,
        EmptyDestinationsNavigator,
        onSpellPreparedStateChanged = {},
        onCastSpellClicked = {},
        onRemoveSpellRequested = {},
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
