package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.change_name
import com.feko.generictabletoprpg.confirm
import com.feko.generictabletoprpg.dismiss
import com.feko.generictabletoprpg.fix_file_shortcut
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.open
import com.feko.generictabletoprpg.remove
import com.feko.generictabletoprpg.shared.common.ui.components.AlertDialogBase
import com.feko.generictabletoprpg.shared.common.ui.components.BoxWithScrollIndicator
import com.feko.generictabletoprpg.shared.common.ui.components.ConfirmationDialog
import com.feko.generictabletoprpg.shared.common.ui.components.DialogButton
import com.feko.generictabletoprpg.shared.common.ui.components.DialogInputField
import com.feko.generictabletoprpg.shared.common.ui.components.DialogTitle
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgContextMenu
import com.feko.generictabletoprpg.shared.common.ui.components.OutlinedDialogButton
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.features.tracker.model.FileShortcutEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.FileShortcutsContainer
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import io.github.vinceglb.filekit.dialogs.compose.PickerResultLauncher
import org.jetbrains.compose.resources.stringResource

@Composable
fun FileShortcutsDialog(
    dialog: ITrackerDialog.FileShortcutsDialog,
    viewModel: TrackerViewModel
) {
    val onDismiss = viewModel::dismissDialog
    AlertDialogBase(
        onDialogDismiss = onDismiss,
        screenHeight = 0.6f,
        dialogTitle = { DialogTitle(dialog.title.text()) },
        dialogButtons = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(stringResource(Res.string.dismiss))
                }
            }
        }
    ) {
        FileShortcutsListContent(
            dialog,
            onClick = viewModel::openFileShortcut,
            onChangeName = {
                viewModel.showChangeFileShortcutNameSecondaryDialog(it)
            },
            onRemove = viewModel::removeFileShortcutRequested
        )
    }
}

@Composable
fun FileShortcutsListContent(
    dialog: ITrackerDialog.FileShortcutsDialog,
    onClick: (FileShortcutEntry) -> Unit,
    onChangeName: (FileShortcutEntry) -> Unit,
    onRemove: (FileShortcutEntry) -> Unit
) {
    val scrollState = rememberLazyListState()
    val dimens = LocalDimens.current
    BoxWithScrollIndicator(
        scrollState,
        backgroundColor = CardDefaults.cardColors().containerColor,
        Modifier.padding(top = dimens.paddingSmall)
    ) {
        LazyColumn(
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(dimens.gapSmall)
        ) {
            items(
                (dialog.fileShortcuts.serializedItem as FileShortcutsContainer).entries,
                key = { it.id }
            ) {
                FileShortcutListItem(
                    it.name,
                    onClick = { onClick(it) },
                    onChangeName = { onChangeName(it) },
                    onRemove = { onRemove(it) }
                )
            }
        }
    }
}

@Composable
fun FileShortcutListItem(
    name: String,
    onClick: () -> Unit,
    onChangeName: () -> Unit,
    onRemove: () -> Unit
) {
    Card(shape = MaterialTheme.shapes.extraLarge) {
        val dimens = LocalDimens.current
        Row(
            Modifier.clickable(onClick = onClick)
                .padding(dimens.paddingSmall)
                .padding(start = dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, modifier = Modifier.weight(1f))
            var contextMenuExpanded by remember { mutableStateOf(false) }
            GttrpgContextMenu(
                contextMenuExpanded,
                { contextMenuExpanded = it }
            ) {
                DropdownMenuItem(
                    { Text(stringResource(Res.string.open)) },
                    onClick = {
                        onClick()
                        contextMenuExpanded = false
                    })
                DropdownMenuItem(
                    { Text(stringResource(Res.string.change_name)) },
                    onClick = {
                        onChangeName()
                        contextMenuExpanded = false
                    })
                DropdownMenuItem(
                    { Text(stringResource(Res.string.remove)) },
                    onClick = {
                        onRemove()
                        contextMenuExpanded = false
                    })
            }
        }
    }
}

@Composable
fun EditFileShortcutNameDialog(
    dialog: ITrackerDialog.EditFileShortcutNameDialog,
    viewModel: TrackerViewModel,
    onDismiss: () -> Unit
) {
    var fileShortcut by remember { mutableStateOf(dialog.fileShortcutEntry) }
    val canConfirmDialog = fileShortcut.isValid()
    AlertDialogBase(
        onDialogDismiss = onDismiss,
        dialogTitle = { DialogTitle(dialog.title.text()) },
        dialogButtons = {
            DialogButton(
                stringResource(Res.string.confirm),
                onClick = {
                    onDismiss()
                    viewModel.createOrEditFileShortcut(dialog.fileShortcuts, fileShortcut)
                },
                isEnabled = canConfirmDialog
            )
        }
    ) {
        DialogInputField(
            fileShortcut.name,
            stringResource(Res.string.name),
            onValueChange = {
                fileShortcut = fileShortcut.copy(name = it)
            },
            isInputFieldValid = { canConfirmDialog },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            autoFocus = true
        )
    }
}

@Composable
fun FileShortcutsSecondaryDialog(
    dialog: ITrackerDialog.FileShortcutsDialog,
    viewModel: TrackerViewModel,
    fixBrokenFileShortcutPickFileLauncher: PickerResultLauncher
) {
    when (dialog.secondaryDialog) {
        is IFileShortcutDialogs.ConfirmItemRemovalDialog ->
            ConfirmationDialog(
                onConfirm = {
                    viewModel.removeFileShortcut(
                        dialog.fileShortcuts,
                        dialog.secondaryDialog.fileShortcut
                    )
                },
                viewModel::dismissFileShortcutSecondaryDialog,
                dialog.secondaryDialog.title.text()
            )

        is ITrackerDialog.EditFileShortcutNameDialog ->
            EditFileShortcutNameDialog(
                dialog.secondaryDialog,
                viewModel,
                viewModel::dismissFileShortcutSecondaryDialog
            )

        is IFileShortcutDialogs.BrokenFileShortcutDialog ->
            BrokenFileShortcutDialog(
                dialog.fileShortcuts,
                dialog.secondaryDialog,
                viewModel,
                fixBrokenFileShortcutPickFileLauncher
            )

        IFileShortcutDialogs.None -> Unit
    }
}

@Composable
fun BrokenFileShortcutDialog(
    fileShortcuts: TrackedThing,
    dialog: IFileShortcutDialogs.BrokenFileShortcutDialog,
    viewModel: TrackerViewModel,
    pickFileLauncher: PickerResultLauncher
) {
    AlertDialogBase(
        viewModel::dismissFileShortcutSecondaryDialog,
        dialogTitle = { DialogTitle(dialog.title.text()) },
        dialogButtons = {
            DialogButton(
                stringResource(Res.string.fix_file_shortcut),
                onClick = {
                    pickFileLauncher.launch()
                })
            OutlinedDialogButton(stringResource(Res.string.remove), onClick = {
                viewModel.removeFileShortcut(fileShortcuts, dialog.fileShortcut)
                viewModel.dismissFileShortcutSecondaryDialog()
            })
        }) {
    }
}
