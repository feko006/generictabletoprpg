package com.feko.generictabletoprpg.shared.features.encounter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.add_lair_actions
import com.feko.generictabletoprpg.armor_class
import com.feko.generictabletoprpg.confirm
import com.feko.generictabletoprpg.delete_dialog_title
import com.feko.generictabletoprpg.health
import com.feko.generictabletoprpg.initiative
import com.feko.generictabletoprpg.legendary_actions
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.shared.common.ui.components.AlertDialogBase
import com.feko.generictabletoprpg.shared.common.ui.components.BoxWithScrollIndicator
import com.feko.generictabletoprpg.shared.common.ui.components.ConfirmationDialog
import com.feko.generictabletoprpg.shared.common.ui.components.DialogInputField
import com.feko.generictabletoprpg.shared.common.ui.components.DialogTitle
import com.feko.generictabletoprpg.shared.common.ui.components.EnterValueDialog
import com.feko.generictabletoprpg.shared.common.ui.components.IInputFieldValueConverter
import com.feko.generictabletoprpg.shared.common.ui.components.NumberDialogInputField
import com.feko.generictabletoprpg.shared.common.ui.components.SelectFromListDialog
import com.feko.generictabletoprpg.shared.features.encounter.InitiativeEntryEntity
import com.feko.generictabletoprpg.spell_attack_modifier
import com.feko.generictabletoprpg.spell_save_dc
import org.jetbrains.compose.resources.stringResource

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

        is IEncounterDialog.PickLegendaryActionDialog ->
            PickLegendaryActionDialog(
                dialog,
                viewModel::useLegendaryActionAndProgressInitiative,
                viewModel::dismissDialog
            )

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
                    ) { Text(stringResource(Res.string.add_lair_actions)) }
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
                ) { Text(stringResource(Res.string.confirm)) }
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
                DialogInputField(
                    value = name,
                    label = stringResource(Res.string.name),
                    onValueChange = {
                        onItemUpdate(initiativeEntry.copy(name = it))
                    },
                    isInputFieldValid = { initiativeEntry.isNameValid },
                    autoFocus = !initiativeEntry.isSavedInDatabase
                )

                NumberDialogInputField(
                    value = initiativeEntry.initiative,
                    label = stringResource(Res.string.initiative),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = { onItemUpdate(initiativeEntry.copy(initiative = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                NumberDialogInputField(
                    value = initiativeEntry.health,
                    label = stringResource(Res.string.health),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = { onItemUpdate(initiativeEntry.copy(health = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                NumberDialogInputField(
                    value = initiativeEntry.armorClass,
                    label = stringResource(Res.string.armor_class),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = { onItemUpdate(initiativeEntry.copy(armorClass = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                NumberDialogInputField(
                    value = initiativeEntry.legendaryActions,
                    label = stringResource(Res.string.legendary_actions),
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

                NumberDialogInputField(
                    value = initiativeEntry.spellSaveDc,
                    label = stringResource(Res.string.spell_save_dc),
                    convertInputValue = IInputFieldValueConverter.IntInputFieldValueConverter,
                    onValueChange = { onItemUpdate(initiativeEntry.copy(spellSaveDc = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                NumberDialogInputField(
                    value = initiativeEntry.spellAttackModifier,
                    label = stringResource(Res.string.spell_attack_modifier),
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
        stringResource(Res.string.delete_dialog_title)
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

@Composable
private fun PickLegendaryActionDialog(
    dialog: IEncounterDialog.PickLegendaryActionDialog,
    onItemSelect: (InitiativeEntryEntity) -> Unit,
    onDismiss: () -> Unit
) {
    SelectFromListDialog(
        dialog.title.text(),
        dialog.entriesWithLegendaryActions,
        getListItemKey = { it.id },
        onItemSelect = onItemSelect,
        onDialogDismiss = onDismiss
    ) {
        ListItem(
            headlineContent = { Text(it.name) },
            trailingContent = { Text(it.printableLegendaryActions) }
        )
    }
}