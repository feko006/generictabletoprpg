package com.feko.generictabletoprpg.tracker.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.DialogProperties
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.asSignedString
import com.feko.generictabletoprpg.common.alertdialog.EmptyAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.composable.CheckboxWithText
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
import com.feko.generictabletoprpg.tracker.StatEntry
import com.feko.generictabletoprpg.tracker.StatSkillEntry
import com.feko.generictabletoprpg.tracker.StatsContainer
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

                DialogType.EditStats -> StatsEditDialog(viewModel.statsEditDialog, defaultName)

                DialogType.PreviewStatSkills -> PreviewStatSkillsDialog(viewModel)

                DialogType.None -> Unit
            }
        }
    }
}

@Composable
fun StatsEditDialog(
    viewModel: IStatsEditDialogSubViewModel,
    defaultName: String
) {
    val editedStats by viewModel.editedStats.collectAsState(null)
    if (editedStats == null) return
    val editedStatsValue = requireNotNull(editedStats)
    val statsContainer = requireNotNull(editedStatsValue.serializedItem)
    DialogBase(
        viewModel,
        Modifier.heightIn(0.dp, (LocalConfiguration.current.screenHeightDp * 0.7f).dp)
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f, false)
        ) {
            Text(
                stringResource(R.string.additional_bonus_hint),
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                style = Typography.bodySmall
            )
            InputField(
                value = editedStatsValue.name,
                label = "${stringResource(R.string.name)} ($defaultName)",
                onValueChange = { viewModel.updateStatsName(it) },
            )
            var proficiencyBonusValue
                    by remember { mutableStateOf(statsContainer.proficiencyBonus.toString()) }
            InputField(
                value = proficiencyBonusValue,
                label = stringResource(R.string.proficiency_bonus),
                onValueChange = {
                    viewModel.updateStatsProficiencyBonus(it)
                    proficiencyBonusValue = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            var initiativeAdditionalBonus by remember {
                mutableStateOf(statsContainer.initiativeAdditionalBonus.toString())
            }
            InputField(
                value = initiativeAdditionalBonus,
                label = stringResource(R.string.initiative_additional_bonus),
                onValueChange = {
                    viewModel.updateStatsInitiativeAdditionalBonus(it)
                    initiativeAdditionalBonus = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            var spellSaveDcAdditionalBonusValue
                    by remember { mutableStateOf(statsContainer.spellSaveDcAdditionalBonus.toString()) }
            InputField(
                value = spellSaveDcAdditionalBonusValue,
                label = stringResource(R.string.spell_save_dc_additional_bonus),
                onValueChange = {
                    viewModel.updateSpellSaveDcAdditionalBonus(it)
                    spellSaveDcAdditionalBonusValue = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            var spellAttackAdditionalBonusValue
                    by remember { mutableStateOf(statsContainer.spellAttackAdditionalBonus.toString()) }
            InputField(
                value = spellAttackAdditionalBonusValue,
                label = stringResource(R.string.spell_attack_additional_bonus),
                onValueChange = {
                    viewModel.updateSpellAttackAdditionalBonus(it)
                    spellAttackAdditionalBonusValue = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            for ((statIndex, statEntry) in statsContainer.stats.withIndex()) {
                Spacer(Modifier.height(8.dp))
                StatsStatEntry(statEntry, statIndex, viewModel, statsContainer)
                if (statIndex < statsContainer.stats.size - 1) {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun PreviewStatSkillsDialog(viewModel: IStatsPreviewDialogTrackerViewModel) {
    Column(
        Modifier
            .padding(16.dp)
            .heightIn(0.dp, (LocalConfiguration.current.screenHeightDp * 0.7f).dp),
    ) {
        DialogTitle(viewModel.alertDialog.titleResource)
        val skills = requireNotNull(viewModel.statsBeingPreviewed)
            .stats
            .flatMap { it.skills }
            .sortedBy { it.name }
        val passiveSkills = skills.filter { it.showPassive }
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f, false)
        ) {
            HeaderWithDividers(stringResource(R.string.passive_skills))
            passiveSkills.forEachIndexed { index, passiveSkill ->
                SkillRow(passiveSkill.name, passiveSkill.passiveScore.toString())
                if (index < passiveSkills.size - 1) {
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
        TextButton(
            onClick = { viewModel.alertDialog.dismiss() },
            modifier = Modifier
                .align(Alignment.End)
                .wrapContentWidth()
        ) { Text(stringResource(R.string.dismiss)) }
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
    statsContainer: StatsContainer
) {
    HeaderWithDividers("${statEntry.name} (${statEntry.shortName})")
    key("editStatEntry-$statIndex") {
        var statValue by remember { mutableStateOf(statEntry.score.toString()) }
        val bonusText = "(${statEntry.bonus.asSignedString()})"
        InputField(
            value = statValue,
            label = "${stringResource(R.string.score)}$bonusText",
            onValueChange = {
                viewModel.updateStatScore(statIndex, it)
                statValue = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        var savingThrowAdditionalBonus by remember {
            mutableStateOf(statEntry.savingThrowAdditionalBonus.toString())
        }
        InputField(
            value = savingThrowAdditionalBonus,
            label = stringResource(R.string.saving_throw_additional_bonus),
            onValueChange = {
                viewModel.updateStatSavingThrowAdditionalBonus(statIndex, it)
                savingThrowAdditionalBonus = it
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
                    viewModel
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
    viewModel: IStatsEditDialogSubViewModel
) {
    HeaderWithDividers(skill.name, Modifier.padding(bottom = 8.dp))
    key("editStatEntry-$statIndex-$skillIndex") {
        var skillAdditionalValue by remember {
            mutableStateOf(skill.additionalBonus.toString())
        }
        val imeAction =
            if (statIndex == statsContainer.stats.size - 1
                && skillIndex == statEntry.skills.size - 1
            ) {
                ImeAction.Done
            } else {
                ImeAction.Next
            }
        InputField(
            value = skillAdditionalValue,
            label = stringResource(R.string.skill_additional_bonus),
            onValueChange = {
                viewModel.updateStatSkillAdditionalBonus(statIndex, skillIndex, it)
                skillAdditionalValue = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = imeAction
            ),
            onFormSubmit = { viewModel.confirmDialogAction() },
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
    modifier: Modifier = Modifier,
    inputFields: @Composable ColumnScope.() -> Unit
) {
    Column(
        Modifier
            .padding(16.dp)
            .then(modifier),
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
            Modifier.heightIn(0.dp, (LocalConfiguration.current.screenHeightDp * 0.7f).dp),
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
