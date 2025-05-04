package com.feko.generictabletoprpg.encounter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.composable.AlertDialogBase
import com.feko.generictabletoprpg.common.composable.BoxWithScrollIndicator
import com.feko.generictabletoprpg.common.composable.ConfirmationDialog
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.common.composable.EnterValueDialog
import com.feko.generictabletoprpg.common.composable.IInputFieldValueConverter
import com.feko.generictabletoprpg.common.composable.InputField
import com.feko.generictabletoprpg.common.composable.NumberInputField


@Composable
fun EncounterAlertDialog(viewModel: EncounterViewModel) {
    val dialog by viewModel.dialog.collectAsState(IEncounterDialog.None)
    EncounterAlertDialog(dialog, viewModel)
}

@Composable
private fun EncounterAlertDialog(dialog: IEncounterDialog, viewModel: EncounterViewModel) {
    when (dialog) {
        is IEncounterDialog.InitiativeDialog ->
            InitiativeDialog(dialog, viewModel::updateInitiative, viewModel::dismissDialog)

        is IEncounterDialog.HealDialog ->
            HealDialog(dialog, viewModel::heal, viewModel::dismissDialog)

        is IEncounterDialog.DamageDialog ->
            DamageDialog(dialog, viewModel::damage, viewModel::dismissDialog)

        is IEncounterDialog.RemoveAfterTakingDamageDialog ->
            RemoveAfterTakingDamageDialog(dialog, viewModel::deleteEntry, viewModel::dismissDialog)

        is IEncounterDialog.EditDialog ->
            EditDialog(
                dialog.title.text(),
                dialog.entry,
                dialog.isLairActionsButtonVisible,
                onItemUpdate = viewModel::editDialogItemUpdated,
                onConfirm = viewModel::createOrUpdateInitiativeEntry,
                onAddLairActions = viewModel::addLairActions,
                onDismiss = viewModel::dismissDialog
            )

        is IEncounterDialog.ConfirmDeletionDialog ->
            ConfirmDeletionDialog(dialog, viewModel::deleteEntry, viewModel::dismissDialog)

        is IEncounterDialog.ConfirmResetDialog ->
            ConfirmResetDialog(dialog, viewModel::resetInitiative, viewModel::dismissDialog)

        IEncounterDialog.None -> Unit
    }
}

@Composable
private fun InitiativeDialog(
    dialog: IEncounterDialog.InitiativeDialog,
    onConfirm: (InitiativeEntryEntity, Int) -> Unit,
    onDismiss: () -> Unit
) {

    EnterValueDialog(
        onConfirm = { onConfirm(dialog.entry, it.toIntOrNull() ?: 0) },
        onDialogDismissed = onDismiss,
        dialogTitle = dialog.title.text(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
private fun HealDialog(
    dialog: IEncounterDialog.HealDialog,
    onConfirm: (InitiativeEntryEntity, Int) -> Unit,
    onDismiss: () -> Unit
) {
    EnterValueDialog(
        onConfirm = { onConfirm(dialog.entry, it.toIntOrNull() ?: 0) },
        onDialogDismissed = onDismiss,
        dialogTitle = dialog.title.text(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
private fun DamageDialog(
    dialog: IEncounterDialog.DamageDialog,
    onConfirm: (InitiativeEntryEntity, Int) -> Unit,
    onDismiss: () -> Unit
) {
    EnterValueDialog(
        onConfirm = { onConfirm(dialog.entry, it.toIntOrNull() ?: 0) },
        onDialogDismissed = onDismiss,
        dialogTitle = dialog.title.text(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
private fun RemoveAfterTakingDamageDialog(
    dialog: IEncounterDialog.RemoveAfterTakingDamageDialog,
    onConfirm: (InitiativeEntryEntity) -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmationDialog(
        onConfirm = { onConfirm(dialog.entry) },
        onDialogDismiss = onDismiss,
        dialogTitle = dialog.title.text(),
        dialogMessage = dialog.message.text()
    )
}

@Composable
fun EditDialog(
    title: String,
    initiativeEntry: InitiativeEntryEntity,
    isLairActionsButtonVisible: Boolean,
    onItemUpdate: (InitiativeEntryEntity) -> Unit,
    onConfirm: (InitiativeEntryEntity) -> Unit,
    onAddLairActions: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialogBase(
        onDismiss,
        screenHeight = 0.6f,
        dialogTitle = { DialogTitle(title) },
        Arrangement.spacedBy(8.dp),
        dialogButtons = {
            Column {
                if (isLairActionsButtonVisible) {
                    TextButton(
                        onClick = {
                            onAddLairActions()
                            onDismiss()
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .wrapContentWidth()
                    ) { Text(stringResource(R.string.add_lair_actions)) }
                }
                TextButton(
                    onClick = {
                        onConfirm(initiativeEntry)
                        onDismiss()
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .wrapContentWidth(),
                    enabled = initiativeEntry.isEntryValid
                ) { Text(stringResource(R.string.confirm)) }
            }
        }) {
        val scrollState = rememberScrollState()
        BoxWithScrollIndicator(
            scrollState,
            backgroundColor = CardDefaults.cardColors().containerColor,
            Modifier.weight(1f)
        ) {
            Column(
                Modifier.verticalScroll(scrollState),
                Arrangement.spacedBy(8.dp)
            ) {
                val name = initiativeEntry.name
                InputField(
                    value = name,
                    label = stringResource(R.string.name),
                    onValueChange = {
                        onItemUpdate(initiativeEntry.copy(name = it))
                    },
                    isInputFieldValid = { initiativeEntry.isNameValid },
                    autoFocus = !initiativeEntry.isSavedInDatabase
                )

                NumberInputField(
                    value = initiativeEntry.initiative,
                    label = stringResource(R.string.initiative),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = { onItemUpdate(initiativeEntry.copy(initiative = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                NumberInputField(
                    value = initiativeEntry.health,
                    label = stringResource(R.string.health),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = { onItemUpdate(initiativeEntry.copy(health = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                NumberInputField(
                    value = initiativeEntry.armorClass,
                    label = stringResource(R.string.armor_class),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = { onItemUpdate(initiativeEntry.copy(armorClass = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                NumberInputField(
                    value = initiativeEntry.legendaryActions,
                    label = stringResource(R.string.legendary_actions),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = {
                        onItemUpdate(
                            initiativeEntry.copy(
                                legendaryActions = it,
                                availableLegendaryActions = it
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                NumberInputField(
                    value = initiativeEntry.spellSaveDc,
                    label = stringResource(R.string.spell_save_dc),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = { onItemUpdate(initiativeEntry.copy(spellSaveDc = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                NumberInputField(
                    value = initiativeEntry.spellAttackModifier,
                    label = stringResource(R.string.spell_attack_modifier),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = {
                        onItemUpdate(
                            initiativeEntry.copy(
                                spellAttackModifier = it
                            )
                        )
                    },
                    onFormSubmit = {
                        onConfirm(initiativeEntry)
                        onDismiss()
                    },
                    canSubmitForm = { initiativeEntry.isEntryValid },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
        }
    }
}

@Composable
private fun ConfirmDeletionDialog(
    dialog: IEncounterDialog.ConfirmDeletionDialog,
    onConfirm: (InitiativeEntryEntity) -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmationDialog(
        onConfirm = { onConfirm(dialog.entry) },
        onDialogDismiss = onDismiss,
        stringResource(R.string.delete_dialog_title)
    )
}

@Composable
private fun ConfirmResetDialog(
    dialog: IEncounterDialog.ConfirmResetDialog,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmationDialog(
        onConfirm = onConfirm,
        onDialogDismiss = onDismiss,
        dialogTitle = dialog.title.text(),
        dialogMessage = dialog.message.text()
    )
}
