package com.feko.generictabletoprpg.encounter

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.feko.generictabletoprpg.common.composable.ConfirmationDialog
import com.feko.generictabletoprpg.common.composable.EnterValueDialog


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
