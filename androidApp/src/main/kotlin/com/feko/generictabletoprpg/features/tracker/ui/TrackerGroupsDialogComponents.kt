package com.feko.generictabletoprpg.features.tracker.ui

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.components.AlertDialogBase
import com.feko.generictabletoprpg.common.ui.components.ConfirmationDialog
import com.feko.generictabletoprpg.common.ui.components.DialogInputField
import com.feko.generictabletoprpg.common.ui.components.DialogTitle
import com.feko.generictabletoprpg.common.ui.components.GttrpgContextMenu
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup
import com.feko.generictabletoprpg.shared.features.tracker.ui.ITrackerGroupDialog
import com.feko.generictabletoprpg.shared.features.tracker.ui.TrackerGroupViewModel

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
    pickDirectoryLauncher: ManagedActivityResultLauncher<Uri?, Uri?>,
    onTrackerGroupClick: (id: Long, name: String) -> Unit
) {
    ListItem(
        headlineContent = { Text((item as INamed).name) },
        trailingContent = {
            var expanded by remember { mutableStateOf(false) }
            GttrpgContextMenu(
                expanded,
                { expanded = it }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.export)) },
                    onClick = {
                        viewModel.export.exportSingleRequested(item)
                        pickDirectoryLauncher.launch(null)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(painterResource(R.drawable.send_to_mobile), "")
                    })
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.edit)) },
                    onClick = {
                        viewModel.editItemRequested(item)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Edit, "")
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.delete)) },
                    onClick = {
                        viewModel.deleteItemRequested(item)
                        expanded = false
                    },
                    leadingIcon = { Icon(Icons.Default.Delete, "") }
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onTrackerGroupClick(item.id, item.name) })
    )
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
            TextButton(onClick = onDialogDismiss) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(
                onClick = {
                    onConfirm(dialogState.trackedThingGroup)
                    onDialogDismiss()
                },
                enabled = dialogState.trackedThingGroup.name.isNotEmpty(),
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    ) {
        DialogInputField(
            dialogState.trackedThingGroup.name,
            stringResource(R.string.name),
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
