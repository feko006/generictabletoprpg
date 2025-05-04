package com.feko.generictabletoprpg.encounter

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.IText
import com.feko.generictabletoprpg.common.composable.EnterValueDialog


sealed interface IEncounterDialog {

    data object None : IEncounterDialog

    data class InitiativeDialog(
        val entry: InitiativeEntryEntity,
        val title: IText = IText.StringResourceText(R.string.initiative)
    ) : IEncounterDialog

    data class HealDialog(
        val entry: InitiativeEntryEntity,
        val title: IText = IText.StringResourceText(R.string.heal_dialog_title)
    ) : IEncounterDialog
}

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

