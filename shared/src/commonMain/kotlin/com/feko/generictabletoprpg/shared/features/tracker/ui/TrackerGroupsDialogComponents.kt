package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.cancel
import com.feko.generictabletoprpg.confirm
import com.feko.generictabletoprpg.delete
import com.feko.generictabletoprpg.edit
import com.feko.generictabletoprpg.export
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.ui.components.AlertDialogBase
import com.feko.generictabletoprpg.shared.common.ui.components.ConfirmationDialog
import com.feko.generictabletoprpg.shared.common.ui.components.DialogButton
import com.feko.generictabletoprpg.shared.common.ui.components.DialogInputField
import com.feko.generictabletoprpg.shared.common.ui.components.DialogTitle
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgContextMenu
import com.feko.generictabletoprpg.shared.common.ui.components.OutlinedDialogButton
import com.feko.generictabletoprpg.shared.common.ui.components.deleteIcon
import com.feko.generictabletoprpg.shared.common.ui.components.editIcon
import com.feko.generictabletoprpg.shared.common.ui.components.sendToMobileIcon
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup
import io.github.vinceglb.filekit.dialogs.compose.SaverResultLauncher
import org.jetbrains.compose.resources.stringResource

@Composable
fun TrackerGroupsAlertDialog(
    dialog: ITrackerGroupDialog,
    viewModel: TrackerGroupViewModel
) {
    when (dialog) {
        is ITrackerGroupDialog.DeleteDialog ->
            DeleteDialog(
                dialog.dialogTitle,
                onConfirm = { viewModel.deleteGroup(dialog.trackedThingGroup) },
                onDialogDismissed = viewModel::dismissDialog
            )

        is ITrackerGroupDialog.EditDialog ->
            EditDialog(
                dialogState = dialog,
                onValueUpdate = viewModel::editDialogValueUpdated,
                onDialogDismiss = viewModel::dismissDialog,
                onConfirm = viewModel::insertOrUpdateGroup
            )

        ITrackerGroupDialog.None -> {}
    }
}

@Composable
fun TrackerGroupListItem(
    item: TrackedThingGroup,
    viewModel: TrackerGroupViewModel,
    fileSaverLauncher: SaverResultLauncher,
    onTrackerGroupClick: (Long, String) -> Unit
) {
    Card(shape = MaterialTheme.shapes.extraLarge) {
        ListItem(
            headlineContent = { Text((item as INamed).name) },
            trailingContent = {
                var expanded by remember { mutableStateOf(false) }
                GttrpgContextMenu(
                    expanded,
                    { expanded = it }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.export)) },
                        onClick = {
                            viewModel.export.exportSingleRequested(item)
                            launchFileSaver(viewModel, fileSaverLauncher)
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(sendToMobileIcon, "")
                        })
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.edit)) },
                        onClick = {
                            viewModel.editItemRequested(item)
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(editIcon, "")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.delete)) },
                        onClick = {
                            viewModel.deleteItemRequested(item)
                            expanded = false
                        },
                        leadingIcon = { Icon(deleteIcon, "") }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onTrackerGroupClick(item.id, item.name) }),
            colors = ListItemDefaults.colors(containerColor = CardDefaults.cardColors().containerColor)
        )
    }
}

fun launchFileSaver(viewModel: TrackerGroupViewModel, fileSaveLauncher: SaverResultLauncher) {
    val displayName = viewModel.export.getExportedFileData()
    fileSaveLauncher.launch(suggestedName = displayName, extension = "json")
}

@Composable
fun DeleteDialog(
    dialogTitle: IText,
    onConfirm: () -> Unit,
    onDialogDismissed: () -> Unit
) {
    ConfirmationDialog(onConfirm, onDialogDismissed, dialogTitle.text())
}

@Composable
fun EditDialog(
    dialogState: ITrackerGroupDialog.EditDialog,
    onValueUpdate: (TrackedThingGroup) -> Unit,
    onDialogDismiss: () -> Unit,
    onConfirm: (TrackedThingGroup) -> Unit
) {
    AlertDialogBase(
        onDialogDismiss,
        dialogTitle = {
            DialogTitle(dialogState.dialogTitle.text())
        },
        dialogButtons = {
            DialogButton(
                stringResource(Res.string.confirm),
                onClick = {
                    onConfirm(dialogState.trackedThingGroup)
                    onDialogDismiss()
                },
                isEnabled = dialogState.trackedThingGroup.name.isNotEmpty(),
            )
            OutlinedDialogButton(stringResource(Res.string.cancel), onDialogDismiss)
        }
    ) {
        DialogInputField(
            dialogState.trackedThingGroup.name,
            stringResource(Res.string.name),
            onValueChange = { onValueUpdate(dialogState.trackedThingGroup.copy(name = it)) },
            onFormSubmit = {
                onConfirm(dialogState.trackedThingGroup)
                onDialogDismiss()
            },
            canSubmitForm = { it.isNotEmpty() },
            isInputFieldValid = { it.isNotEmpty() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            autoFocus = true
        )
    }
}