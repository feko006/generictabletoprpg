package com.feko.generictabletoprpg.tracker

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.documentfile.provider.DocumentFile
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.ButtonState
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.RootDestinations
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.IText
import com.feko.generictabletoprpg.common.INamed
import com.feko.generictabletoprpg.common.composable.AddFABButton
import com.feko.generictabletoprpg.common.composable.AlertDialogBase
import com.feko.generictabletoprpg.common.composable.ConfirmationDialog
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.common.composable.InputField
import com.feko.generictabletoprpg.common.composable.OverviewScreen
import com.feko.generictabletoprpg.common.composable.ToastMessage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.TrackerScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>(start = true)
@Composable
fun TrackerGroupsScreen(
    navigator: DestinationsNavigator,
    appViewModel: AppViewModel
) {
    val viewModel: TrackerGroupViewModel = koinViewModel()
    val context = LocalContext.current
    ToastMessage(viewModel.export.toast)
    val pickDirectoryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocumentTree()
        ) launch@{ directoryUri ->
            onDirectorySelected(directoryUri, viewModel, context)
        }
    val exportButtonVisible by viewModel.exportButtonVisible.collectAsState(false)
    val navBarActions = mutableListOf<ButtonState>()
    if (exportButtonVisible) {
        navBarActions.add(
            ButtonState(
                painter = painterResource(R.drawable.send_to_mobile)
            ) {
                viewModel.export.exportAllRequested()
                pickDirectoryLauncher.launch(null)
            }
        )
    }
    appViewModel.run {
        set(
            appBarTitle = stringResource(R.string.tracker_title),
            navBarActions = navBarActions
        )
        updateActiveDrawerItem(RootDestinations.Tracker)
    }
    val refreshables by appViewModel.refreshesPending.collectAsState()
    if (refreshables.contains(RootDestinations.Tracker)) {
        viewModel.refreshItems()
        appViewModel.itemsRefreshed(RootDestinations.Tracker)
    }
    OverviewScreen(
        viewModel = viewModel,
        listItem = { item, _, _ ->
            TrackerGroupListItem(
                item = item,
                navigator = navigator,
                viewModel = viewModel,
                pickDirectoryLauncher
            )
        },
        fabButton = { modifier ->
            AddFABButton(modifier) {
                viewModel.newTrackedThingGroupRequested()
            }
        }
    )
    val dialog by viewModel.dialog.collectAsState(ITrackerGroupDialog.None)
    TrackerGroupsAlertDialog(dialog, viewModel)
}

@Composable
private fun TrackerGroupsAlertDialog(
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

private fun onDirectorySelected(
    directoryUri: Uri?,
    viewModel: TrackerGroupViewModel,
    context: Context
) {
    if (directoryUri == null) {
        viewModel.export.notifyCancelled()
        return
    }
    try {
        val directoryFile = DocumentFile.fromTreeUri(context, directoryUri)
        val (mimeType, displayName) = viewModel.export.getExportedFileData()
        val newFile = directoryFile!!.createFile(mimeType, displayName)
        CoroutineScope(Dispatchers.Default).launch {
            context.contentResolver
                .openOutputStream(newFile!!.uri)
                .use {
                    viewModel.export.exportData(it)
                }
        }
    } catch (e: Exception) {
        viewModel.export.notifyFailed(e)
    }
}

@Composable
private fun TrackerGroupListItem(
    item: TrackedThingGroup,
    navigator: DestinationsNavigator,
    viewModel: TrackerGroupViewModel,
    pickDirectoryLauncher: ManagedActivityResultLauncher<Uri?, Uri?>,
) {
    ListItem(
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text((item as INamed).name, modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    viewModel.export.exportSingleRequested(item)
                    pickDirectoryLauncher.launch(null)
                }) {
                    Icon(painterResource(R.drawable.send_to_mobile), "")
                }
                IconButton(onClick = { viewModel.editItemRequested(item) }) {
                    Icon(Icons.Default.Edit, "")
                }
                IconButton(onClick = { viewModel.deleteItemRequested(item) }) {
                    Icon(Icons.Default.Delete, "")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigator.navigate(TrackerScreenDestination(item.id, item.name))
            })
}

@Composable
private fun DeleteDialog(
    dialogTitle: IText,
    onConfirm: () -> Unit,
    onDialogDismissed: () -> Unit
) {
    ConfirmationDialog(onConfirm, onDialogDismissed, dialogTitle.text())
}

@Composable
private fun EditDialog(
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
        InputField(
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