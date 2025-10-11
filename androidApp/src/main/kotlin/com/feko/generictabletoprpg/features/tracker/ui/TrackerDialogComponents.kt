package com.feko.generictabletoprpg.features.tracker.ui


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
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.common.ui.components.AlertDialogBase
import com.feko.generictabletoprpg.common.ui.components.BoxWithScrollIndicator
import com.feko.generictabletoprpg.common.ui.components.CheckboxWithText
import com.feko.generictabletoprpg.common.ui.components.ConfirmationDialog
import com.feko.generictabletoprpg.common.ui.components.DialogInputField
import com.feko.generictabletoprpg.common.ui.components.DialogTitle
import com.feko.generictabletoprpg.common.ui.components.EnterValueDialog
import com.feko.generictabletoprpg.common.ui.components.IInputFieldValueConverter
import com.feko.generictabletoprpg.common.ui.components.NumberDialogInputField
import com.feko.generictabletoprpg.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.common.ui.theme.Typography
import com.feko.generictabletoprpg.expertise
import com.feko.generictabletoprpg.proficiency
import com.feko.generictabletoprpg.saving_throw_proficiency
import com.feko.generictabletoprpg.shared.common.domain.asSignedString
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.tracker.model.StatEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.StatSkillEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.StatsContainer
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import com.feko.generictabletoprpg.shared.features.tracker.model.amount
import com.feko.generictabletoprpg.shared.features.tracker.model.isIntBased
import com.feko.generictabletoprpg.shared.features.tracker.model.isLevelValid
import com.feko.generictabletoprpg.shared.features.tracker.model.isValueValid
import com.feko.generictabletoprpg.shared.features.tracker.model.setNewValue
import com.feko.generictabletoprpg.shared.features.tracker.model.validate
import com.feko.generictabletoprpg.shared.features.tracker.ui.ITrackerDialog
import com.feko.generictabletoprpg.shared.features.tracker.ui.TrackerViewModel
import com.feko.generictabletoprpg.spellcasting_modifier

@Composable
fun TrackerAlertDialog(viewModel: TrackerViewModel, onSpellClick: (Spell) -> Unit) {
    val dialog by viewModel.dialog.collectAsState(ITrackerDialog.None)
    TrackerAlertDialog(dialog, viewModel, onSpellClick)
}

@Composable
private fun TrackerAlertDialog(
    dialog: ITrackerDialog,
    viewModel: TrackerViewModel,
    onSpellClick: (Spell) -> Unit,
) {
    when (dialog) {
        is ITrackerDialog.SpellListDialog ->
            SpellListDialogWithViewModel(dialog, viewModel, onSpellClick)

        is ITrackerDialog.ConfirmDeletionDialog ->
            ConfirmDeletionDialog(dialog, viewModel::deleteTrackedThing, viewModel::dismissDialog)

        is ITrackerDialog.RefreshAllDialog ->
            RefreshAllDialog(dialog, viewModel::refreshAll, viewModel::dismissDialog)

        is ITrackerDialog.AddToPercentageDialog ->
            AddToPercentageDialog(dialog, { percentage, amount ->
                viewModel.addToTrackedThing(percentage, amount)
            }, viewModel::dismissDialog)

        is ITrackerDialog.SubtractFromPercentageDialog ->
            SubtractFromPercentageDialog(
                dialog,
                { percentage, amount ->
                    viewModel.subtractFromTrackedThing(percentage, amount)
                },
                viewModel::dismissDialog
            )

        is ITrackerDialog.AddToNumberDialog ->
            AddToNumberDialog(dialog, { number, amount ->
                viewModel.addToTrackedThing(number, amount)
            }, viewModel::dismissDialog)

        is ITrackerDialog.SubtractFromNumberDialog ->
            SubtractFromNumberDialog(
                dialog,
                { number, amount ->
                    viewModel.subtractFromTrackedThing(number, amount)
                },
                viewModel::dismissDialog
            )

        is ITrackerDialog.DamageHealthDialog ->
            DamageHealthDialog(dialog, { health, amount ->
                viewModel.subtractFromTrackedThing(health, amount)
            }, viewModel::dismissDialog)

        is ITrackerDialog.HealHealthDialog ->
            HealHealthDialog(dialog, { health, amount ->
                viewModel.addToTrackedThing(health, amount)
            }, viewModel::dismissDialog)

        is ITrackerDialog.AddTemporaryHpDialog ->
            AddTemporaryHpDialog(dialog, viewModel::addTemporaryHp, viewModel::dismissDialog)

        is ITrackerDialog.PreviewStatSkillsDialog ->
            PreviewStatSkillsDialog(dialog, viewModel::dismissDialog)

        is ITrackerDialog.EditDialog ->
            EditDialog(
                dialog,
                viewModel.groupName,
                viewModel::createOrEditTrackedThing,
                viewModel::editDialogValueUpdated,
                viewModel::dismissDialog
            )

        is ITrackerDialog.StatsEditDialog ->
            StatsEditDialog(
                dialog,
                viewModel.groupName,
                viewModel::insertOrUpdateStats,
                viewModel::editStatsDialogValueUpdated,
                viewModel::dismissDialog
            )

        is ITrackerDialog.None -> Unit
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
    onConfirm: (TrackedThing, String) -> Unit,
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
    onConfirm: (TrackedThing, String) -> Unit,
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
    onConfirm: (TrackedThing, String) -> Unit,
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
    onConfirm: (TrackedThing, String) -> Unit,
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
    onConfirm: (TrackedThing, String) -> Unit,
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
    onConfirm: (TrackedThing, String) -> Unit,
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
    onConfirm: (TrackedThing, String) -> Unit,
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
fun PreviewStatSkillsDialog(
    dialog: ITrackerDialog.PreviewStatSkillsDialog,
    onDismiss: () -> Unit
) {
    AlertDialogBase(
        onDialogDismiss = onDismiss,
        screenHeight = 0.7f,
        dialogTitle = { DialogTitle(dialog.title.text()) },
        Arrangement.Top,
        dialogButtons = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.wrapContentWidth()
            ) { Text(stringResource(R.string.dismiss)) }
        }
    ) {
        val statsContainer = dialog.stats
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
    dialog: ITrackerDialog.EditDialog,
    defaultName: String,
    onConfirm: (TrackedThing) -> Unit,
    onValueUpdate: (TrackedThing) -> Unit,
    onDismiss: () -> Unit
) {
    val editedTrackedThing = dialog.editedItem
    val canConfirmEditOperation = editedTrackedThing.validate()
    AlertDialogBase(
        onDialogDismiss = onDismiss,
        dialogTitle = { DialogTitle(dialog.title.text()) },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        dialogButtons = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(
                onClick = {
                    onConfirm(dialog.editedItem)
                    onDismiss()
                },
                enabled = canConfirmEditOperation,
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    ) {
        val isSpellList = editedTrackedThing.type == TrackedThing.Type.SpellList
        val onFormSubmit = {
            if (canConfirmEditOperation) {
                onConfirm(dialog.editedItem)
                onDismiss()
            }
        }
        DialogInputField(
            editedTrackedThing.name,
            "${stringResource(R.string.name)} ($defaultName)",
            onValueChange = {
                onValueUpdate(editedTrackedThing.copy(name = it))
            },
            onFormSubmit = onFormSubmit,
            keyboardOptions = KeyboardOptions(
                imeAction = if (isSpellList) ImeAction.Done else ImeAction.Next
            ),
            autoFocus = true
        )
        val isSpellSlot = editedTrackedThing.type == TrackedThing.Type.SpellSlot
        if (isSpellSlot) {
            NumberDialogInputField(
                value = editedTrackedThing.level,
                label = stringResource(R.string.level),
                convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                onValueChange = {
                    onValueUpdate(editedTrackedThing.copy(level = it))
                },
                isInputFieldValid = { editedTrackedThing.isLevelValid },
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
                onValueUpdate(
                    editedTrackedThing
                        .copy()
                        .apply {
                            setNewValue(it)
                            managedDefaultValue = it
                        }
                )
            },
            onFormSubmit
        )
    }
}

@Composable
private fun EditDialogValueInputField(
    isSpellList: Boolean,
    editedTrackedThing: TrackedThing,
    onValueChange: (String) -> Unit,
    onFormSubmit: () -> Unit
) {
    if (isSpellList) return

    if (editedTrackedThing.type == TrackedThing.Type.Text) {
        DialogInputField(
            value = editedTrackedThing.value,
            label = stringResource(id = R.string.text),
            onValueChange = onValueChange,
            onFormSubmit = onFormSubmit,
            isInputFieldValid = { editedTrackedThing.isValueValid() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.None
            ),
            maxLines = 5
        )
    }

    val isPercentage = editedTrackedThing.type == TrackedThing.Type.Percentage
    if (isPercentage) {
        NumberDialogInputField(
            editedTrackedThing.amount.toFloat(),
            label = stringResource(R.string.amount),
            convertInputValue = IInputFieldValueConverter.FloatInputFieldValueConverter,
            onValueChange = { onValueChange(it.toString()) },
            canSubmitForm = { editedTrackedThing.isValueValid() },
            onFormSubmit = onFormSubmit,
            isInputFieldValid = { editedTrackedThing.isValueValid() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            suffix = { Text("%") }
        )
    }

    val isIntTrackedThing = editedTrackedThing.isIntBased
    if (isIntTrackedThing) {
        NumberDialogInputField(
            editedTrackedThing.amount.toInt(),
            label = stringResource(R.string.amount),
            convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
            onValueChange = { onValueChange(it.toString()) },
            canSubmitForm = { editedTrackedThing.isValueValid() },
            onFormSubmit = onFormSubmit,
            isInputFieldValid = { editedTrackedThing.isValueValid() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
fun StatsEditDialog(
    dialog: ITrackerDialog.StatsEditDialog,
    defaultName: String,
    onFormSubmit: (TrackedThing) -> Unit,
    onValueUpdate: (TrackedThing) -> Unit,
    onDismiss: () -> Unit
) {
    val editedStats = dialog.stats
    val statsContainer = requireNotNull(editedStats.serializedItem) as StatsContainer
    AlertDialogBase(
        onDialogDismiss = onDismiss,
        screenHeight = 0.6f,
        dialogTitle = { DialogTitle(dialog.title.text()) },
        dialogButtons = {
            TextButton(
                onClick = {
                    onFormSubmit(editedStats)
                    onDismiss()
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
            Modifier
                .weight(1f)
                .padding(top = 8.dp)
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
                DialogInputField(
                    value = editedStats.name,
                    label = "${stringResource(R.string.name)} ($defaultName)",
                    onValueChange = { onValueUpdate(editedStats.copy(name = it)) }
                )
                NumberDialogInputField(
                    value = statsContainer.proficiencyBonus,
                    label = stringResource(R.string.proficiency_bonus),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = {
                        onValueUpdate(
                            editedStats.copy(serializedItem = statsContainer.copy(proficiencyBonus = it))
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                NumberDialogInputField(
                    value = statsContainer.initiativeAdditionalBonus,
                    label = stringResource(R.string.initiative_additional_bonus),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = {
                        onValueUpdate(
                            editedStats.copy(
                                serializedItem = statsContainer.copy(initiativeAdditionalBonus = it)
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                NumberDialogInputField(
                    value = statsContainer.spellSaveDcAdditionalBonus,
                    label = stringResource(R.string.spell_save_dc_additional_bonus),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = {
                        onValueUpdate(
                            editedStats.copy(
                                serializedItem = statsContainer.copy(spellSaveDcAdditionalBonus = it)
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                NumberDialogInputField(
                    value = statsContainer.spellAttackAdditionalBonus,
                    label = stringResource(R.string.spell_attack_additional_bonus),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = {
                        onValueUpdate(
                            editedStats.copy(
                                serializedItem = statsContainer.copy(spellAttackAdditionalBonus = it)
                            )
                        )
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
                        statsContainer,
                        onValueUpdate = {
                            onValueUpdate(editedStats.copy(serializedItem = it))
                        },
                        onFormSubmit = {
                            onFormSubmit(editedStats)
                            onDismiss()
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
    statsContainer: StatsContainer,
    onValueUpdate: (StatsContainer) -> Unit,
    onFormSubmit: () -> Unit
) {
    HeaderWithDividers("${statEntry.name} (${statEntry.shortName})")
    key("editStatEntry-$statIndex") {
        val bonusText = "(${statEntry.bonus.asSignedString()})"
        NumberDialogInputField(
            value = statEntry.score,
            label = "${stringResource(R.string.score)}$bonusText",
            convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
            onValueChange = {
                val newStatEntry = statsContainer.stats[statIndex].copy(score = it)
                onValueUpdate(
                    statsContainer.copy(
                        stats = statsContainer.stats.mapIndexed { index, statEntry ->
                            if (index == statIndex) newStatEntry else statEntry
                        })
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        NumberDialogInputField(
            value = statEntry.savingThrowAdditionalBonus,
            label = stringResource(R.string.saving_throw_additional_bonus),
            convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
            onValueChange = {
                val newStatEntry =
                    statsContainer.stats[statIndex].copy(savingThrowAdditionalBonus = it)
                onValueUpdate(
                    statsContainer.copy(
                        stats = statsContainer.stats.mapIndexed { index, statEntry ->
                            if (index == statIndex) newStatEntry else statEntry
                        })
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
    }
    CheckboxWithText(
        statEntry.isProficientInSavingThrow,
        Res.string.saving_throw_proficiency.asText()
    ) { checked ->
        val newStatEntry =
            statsContainer.stats[statIndex].copy(isProficientInSavingThrow = checked)
        onValueUpdate(
            statsContainer.copy(
                stats = statsContainer.stats.mapIndexed { index, statEntry ->
                    if (index == statIndex) newStatEntry else statEntry
                })
        )
    }
    CheckboxWithText(
        statEntry.isSpellcastingModifier,
        Res.string.spellcasting_modifier.asText()
    ) { checked ->
        val newStatEntry =
            statsContainer.stats[statIndex].copy(isSpellcastingModifier = checked)
        onValueUpdate(
            statsContainer.copy(
                stats = statsContainer.stats.mapIndexed { index, statEntry ->
                    if (index == statIndex) newStatEntry else statEntry
                })
        )
    }
    if (statEntry.skills.isNotEmpty()) {
        Column(
            Modifier.padding(8.dp),
            Arrangement.spacedBy(LocalDimens.current.gapSmall)
        ) {
            for ((skillIndex, skill) in statEntry.skills.withIndex()) {
                StatsStatEntrySkill(
                    skill,
                    statIndex,
                    skillIndex,
                    statsContainer,
                    statEntry,
                    onValueUpdate,
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
    onValueUpdate: (StatsContainer) -> Unit,
    onFormSubmit: () -> Unit
) {
    Column(
        Modifier
            .background(
                CardDefaults.elevatedCardColors().containerColor,
                shape = RoundedCornerShape(8.dp)
            )
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
            NumberDialogInputField(
                value = skill.additionalBonus,
                label = stringResource(R.string.skill_additional_bonus),
                convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                onValueChange = { bonus ->
                    val newStatEntry =
                        statsContainer.stats[statIndex].let {
                            val newSkill = it.skills[skillIndex].copy(additionalBonus = bonus)
                            it.copy(
                                skills = it.skills.mapIndexed { index, skill ->
                                    if (index == skillIndex) newSkill else skill
                                }
                            )
                        }
                    onValueUpdate(
                        statsContainer.copy(
                            stats = statsContainer.stats.mapIndexed { index, statEntry ->
                                if (index == statIndex) newStatEntry else statEntry
                            })
                    )
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
            Res.string.proficiency.asText()
        ) { checked ->
            val newStatEntry =
                statsContainer.stats[statIndex].let {
                    val newSkill = it.skills[skillIndex].run {
                        copy(isProficient = checked, hasExpertise = checked && hasExpertise)
                    }
                    it.copy(
                        skills = it.skills.mapIndexed { index, skill ->
                            if (index == skillIndex) newSkill else skill
                        }
                    )
                }
            onValueUpdate(
                statsContainer.copy(
                    stats = statsContainer.stats.mapIndexed { index, statEntry ->
                        if (index == statIndex) newStatEntry else statEntry
                    })
            )
        }
        CheckboxWithText(
            skill.hasExpertise,
            Res.string.expertise.asText()
        ) { checked ->
            val newStatEntry =
                statsContainer.stats[statIndex].let {
                    val newSkill = it.skills[skillIndex].run {
                        copy(isProficient = checked || isProficient, hasExpertise = checked)
                    }
                    it.copy(
                        skills = it.skills.mapIndexed { index, skill ->
                            if (index == skillIndex) newSkill else skill
                        }
                    )
                }
            onValueUpdate(
                statsContainer.copy(
                    stats = statsContainer.stats.mapIndexed { index, statEntry ->
                        if (index == statIndex) newStatEntry else statEntry
                    })
            )
        }
    }
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