package com.feko.generictabletoprpg.tracker.dialogs


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.asSignedString
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.dialogs.SpellListDialogWithViewModel
import com.feko.generictabletoprpg.common.composable.AlertDialogBase
import com.feko.generictabletoprpg.common.composable.BoxWithScrollIndicator
import com.feko.generictabletoprpg.common.composable.CheckboxWithText
import com.feko.generictabletoprpg.common.composable.ConfirmationDialog
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.common.composable.EnterValueDialog
import com.feko.generictabletoprpg.common.composable.IInputFieldValueConverter
import com.feko.generictabletoprpg.common.composable.InputField
import com.feko.generictabletoprpg.common.composable.NumberInputField
import com.feko.generictabletoprpg.theme.Typography
import com.feko.generictabletoprpg.tracker.Health
import com.feko.generictabletoprpg.tracker.IntTrackedThing
import com.feko.generictabletoprpg.tracker.Number
import com.feko.generictabletoprpg.tracker.Percentage
import com.feko.generictabletoprpg.tracker.SpellList
import com.feko.generictabletoprpg.tracker.SpellSlot
import com.feko.generictabletoprpg.tracker.StatEntry
import com.feko.generictabletoprpg.tracker.StatSkillEntry
import com.feko.generictabletoprpg.tracker.Stats
import com.feko.generictabletoprpg.tracker.StatsContainer
import com.feko.generictabletoprpg.tracker.Text
import com.feko.generictabletoprpg.tracker.TrackedThing
import com.feko.generictabletoprpg.tracker.TrackerViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun TrackerAlertDialog(viewModel: TrackerViewModel, navigator: DestinationsNavigator) {
    val dialog by viewModel.dialog.collectAsState(ITrackerDialog.None)
    AlertDialog(dialog, viewModel, navigator)

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
private fun AlertDialog(
    dialog: ITrackerDialog,
    viewModel: TrackerViewModel,
    navigator: DestinationsNavigator
) {
    when (dialog) {
        is ITrackerDialog.SpellListDialog ->
            SpellListDialogWithViewModel(dialog, viewModel, navigator)

        is ITrackerDialog.ConfirmDeletionDialog ->
            ConfirmDeletionDialog(dialog, viewModel::deleteTrackedThing, viewModel::dismissDialog)

        is ITrackerDialog.RefreshAllDialog ->
            RefreshAllDialog(dialog, viewModel::refreshAll, viewModel::dismissDialog)

        is ITrackerDialog.AddToPercentageDialog ->
            AddToPercentageDialog(dialog, viewModel::addToPercentage, viewModel::dismissDialog)

        is ITrackerDialog.SubtractFromPercentageDialog ->
            SubtractFromPercentageDialog(
                dialog,
                viewModel::subtractFromPercentage,
                viewModel::dismissDialog
            )

        is ITrackerDialog.AddToNumberDialog ->
            AddToNumberDialog(dialog, viewModel::addToNumber, viewModel::dismissDialog)

        is ITrackerDialog.SubtractFromNumberDialog ->
            SubtractFromNumberDialog(
                dialog,
                viewModel::subtractFromNumber,
                viewModel::dismissDialog
            )

        is ITrackerDialog.DamageHealthDialog ->
            DamageHealthDialog(dialog, viewModel::damageHealth, viewModel::dismissDialog)

        is ITrackerDialog.HealHealthDialog ->
            HealHealthDialog(dialog, viewModel::healHealth, viewModel::dismissDialog)

        is ITrackerDialog.AddTemporaryHpDialog ->
            AddTemporaryHpDialog(dialog, viewModel::addTemporaryHp, viewModel::dismissDialog)

        is ITrackerDialog.None -> {
        }
    }
}

@Composable
fun ConfirmDeletionDialog(
    dialog: ITrackerDialog.ConfirmDeletionDialog,
    onConfirm: (TrackedThing) -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmationDialog(
        onConfirm = { onConfirm(dialog.itemToDelete) },
        onDismiss,
        dialogTitle = dialog.title.text()
    )
}

@Composable
fun RefreshAllDialog(
    dialog: ITrackerDialog.RefreshAllDialog,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmationDialog(onConfirm, onDismiss, dialogTitle = dialog.title.text())
}

@Composable
fun AddToPercentageDialog(
    dialog: ITrackerDialog.AddToPercentageDialog,
    onConfirm: (Percentage, String) -> Unit,
    onDismiss: () -> Unit
) {
    EnterValueDialog(
        onConfirm = { onConfirm(dialog.percentage, it) },
        onDialogDismissed = onDismiss,
        dialogTitle = dialog.title.text(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        suffix = { Text("%") }
    )
}

@Composable
fun SubtractFromPercentageDialog(
    dialog: ITrackerDialog.SubtractFromPercentageDialog,
    onConfirm: (Percentage, String) -> Unit,
    onDismiss: () -> Unit
) {
    EnterValueDialog(
        onConfirm = { onConfirm(dialog.percentage, it) },
        onDialogDismissed = onDismiss,
        dialogTitle = dialog.title.text(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        suffix = { Text("%") }
    )
}

@Composable
fun AddToNumberDialog(
    dialog: ITrackerDialog.AddToNumberDialog,
    onConfirm: (Number, String) -> Unit,
    onDismiss: () -> Unit
) {
    EnterValueDialog(
        onConfirm = { onConfirm(dialog.number, it) },
        onDialogDismissed = onDismiss,
        dialogTitle = dialog.title.text(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun SubtractFromNumberDialog(
    dialog: ITrackerDialog.SubtractFromNumberDialog,
    onConfirm: (Number, String) -> Unit,
    onDismiss: () -> Unit
) {
    EnterValueDialog(
        onConfirm = { onConfirm(dialog.number, it) },
        onDialogDismissed = onDismiss,
        dialogTitle = dialog.title.text(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun HealHealthDialog(
    dialog: ITrackerDialog.HealHealthDialog,
    onConfirm: (Health, String) -> Unit,
    onDismiss: () -> Unit
) {
    EnterValueDialog(
        onConfirm = { onConfirm(dialog.health, it) },
        onDialogDismissed = onDismiss,
        dialogTitle = dialog.title.text(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun DamageHealthDialog(
    dialog: ITrackerDialog.DamageHealthDialog,
    onConfirm: (Health, String) -> Unit,
    onDismiss: () -> Unit
) {
    EnterValueDialog(
        onConfirm = { onConfirm(dialog.health, it) },
        onDialogDismissed = onDismiss,
        dialogTitle = dialog.title.text(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun AddTemporaryHpDialog(
    dialog: ITrackerDialog.AddTemporaryHpDialog,
    onConfirm: (Health, String) -> Unit,
    onDismiss: () -> Unit
) {
    EnterValueDialog(
        onConfirm = { onConfirm(dialog.health, it) },
        onDialogDismissed = onDismiss,
        dialogTitle = dialog.title.text(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
fun PreviewStatSkillsDialog(viewModel: TrackerViewModel) {
    val isDialogVisible by viewModel.showStatsDialog.isVisible.collectAsState(false)
    if (!isDialogVisible) return

    AlertDialogBase(
        onDialogDismiss = { viewModel.showStatsDialog.dismiss() },
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
        onDialogDismiss = { viewModel.editDialog.dismiss() },
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
        onDialogDismiss = { viewModel.alertDialog.dismiss() },
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

