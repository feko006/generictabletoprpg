package com.feko.generictabletoprpg.features.tracker.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.documentfile.provider.DocumentFile
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.common.ui.viewmodel.ButtonState
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.RootDestinations
import com.feko.generictabletoprpg.common.ui.components.AddFABButton
import com.feko.generictabletoprpg.common.ui.components.OverviewScreen
import com.feko.generictabletoprpg.common.ui.components.ToastMessage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
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
