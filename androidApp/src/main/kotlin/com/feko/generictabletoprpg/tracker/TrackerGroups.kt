package com.feko.generictabletoprpg.tracker

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.documentfile.provider.DocumentFile
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.ButtonState
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.RootDestinations
import com.feko.generictabletoprpg.common.INamed
import com.feko.generictabletoprpg.common.composable.AddFABButton
import com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.common.composable.OverviewScreen
import com.feko.generictabletoprpg.destinations.TrackerScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun TrackerGroupsScreen(
    navigator: DestinationsNavigator,
    appViewModel: AppViewModel
) {
    val viewModel: TrackerGroupViewModel = koinViewModel()
    val context = LocalContext.current
    val exportToastMessageResource by viewModel.exportToastMessage.collectAsState(0)
    if (exportToastMessageResource != 0) {
        Toast
            .makeText(
                context,
                exportToastMessageResource,
                Toast.LENGTH_SHORT
            )
            .show()
        viewModel.exportToastMessageConsumed()
    }
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
                viewModel.exportAllRequested()
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
    val isAlertDialogVisible by viewModel.isDialogVisible.collectAsState(false)
    OverviewScreen(
        viewModel = viewModel,
        listItem = { item, _, _ ->
            OverviewListItem(
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
        },
        isAlertDialogVisible = isAlertDialogVisible,
        alertDialogComposable = {
            AlertDialogComposable(viewModel)
        }
    )
}

private fun onDirectorySelected(
    directoryUri: Uri?,
    viewModel: TrackerGroupViewModel,
    context: Context
) {
    if (directoryUri == null) {
        viewModel.exportCancelled()
        return
    }
    try {
        val directoryFile = DocumentFile.fromTreeUri(context, directoryUri)
        val (mimeType, displayName) = viewModel.getExportedFileData()
        val newFile = directoryFile!!.createFile(mimeType, displayName)
        CoroutineScope(Dispatchers.Default).launch {
            context.contentResolver
                .openOutputStream(newFile!!.uri)
                .use {
                    viewModel.exportData(it)
                }
        }
    } catch (e: Exception) {
        viewModel.exportFailed(e)
    }
}

@Composable
fun OverviewListItem(
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
                    viewModel.exportSingleRequested(item)
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
@OptIn(ExperimentalMaterial3Api::class)
fun AlertDialogComposable(viewModel: TrackerGroupViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.hideDialog() },
        properties = DialogProperties()
    ) {
        Card {
            Column(
                Modifier.padding(16.dp),
                Arrangement.spacedBy(16.dp)
            ) {
                DialogTitle(viewModel.dialogTitleResource)
                val focusRequester = remember { FocusRequester() }
                val nameInputData by viewModel.groupName.collectAsState()
                if (viewModel.dialogType == TrackerGroupViewModel.DialogType.NewOrUpdate) {
                    TextField(
                        value = nameInputData.value,
                        onValueChange = { viewModel.setName(it) },
                        isError = !nameInputData.isValid,
                        label = {
                            Text(
                                stringResource(R.string.name),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { viewModel.setName("") }
                            ) {
                                Icon(Icons.Default.Clear, "")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(imeAction = Done),
                        keyboardActions = KeyboardActions(
                            onDone = { viewModel.confirmDialogAction() }
                        )
                    )
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                }
                Row {
                    Spacer(Modifier.weight(1f))
                    if (viewModel.dialogType == TrackerGroupViewModel.DialogType.Delete) {
                        TextButton(
                            onClick = { viewModel.hideDialog() },
                            enabled = true,
                            modifier = Modifier.wrapContentWidth()
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                    val buttonEnabled by viewModel.confirmButtonEnabled.collectAsState()
                    TextButton(
                        onClick = { viewModel.confirmDialogAction() },
                        enabled = buttonEnabled,
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}