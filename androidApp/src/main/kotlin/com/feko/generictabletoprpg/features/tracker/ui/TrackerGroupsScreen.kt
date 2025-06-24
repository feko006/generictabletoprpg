package com.feko.generictabletoprpg.features.tracker.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.documentfile.provider.DocumentFile
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.common.ui.RootDestinations
import com.feko.generictabletoprpg.common.ui.components.AddFABButton
import com.feko.generictabletoprpg.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.common.ui.components.SearchableLazyList
import com.feko.generictabletoprpg.common.ui.components.ToastMessage
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackerGroupsScreen(
    appViewModel: AppViewModel,
    onNavigationIconClick: () -> Unit,
    onNavigateToTrackerScreen: (Long, String) -> Unit
) {
    val viewModel: TrackerGroupViewModel = koinViewModel()
    val context = LocalContext.current
    val pickDirectoryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocumentTree()
        ) launch@{ directoryUri ->
            onDirectorySelected(directoryUri, viewModel, context)
        }
    appViewModel.updateActiveDrawerItem(RootDestinations.Tracker.destination)
    Scaffold(
        topBar = {
            GttrpgTopAppBar(R.string.tracker_title.asText(), onNavigationIconClick) {
                val exportButtonVisible by viewModel.exportButtonVisible.collectAsState(false)
                if (exportButtonVisible) {
                    IconButton(onClick = {
                        viewModel.export.exportAllRequested()
                        pickDirectoryLauncher.launch(null)
                    }) { Icon(painterResource(R.drawable.send_to_mobile), "") }
                }
            }
        },
        floatingActionButton = { AddFABButton { viewModel.newTrackedThingGroupRequested() } }
    ) { paddingValues ->
        SearchableLazyList(
            viewModel = viewModel,
            listItem = { item, _, _ ->
                TrackerGroupListItem(
                    item = item,
                    viewModel = viewModel,
                    pickDirectoryLauncher,
                    onNavigateToTrackerScreen
                )
            },
            Modifier.padding(paddingValues),
            addFabButtonSpacerToList = true
        )
    }
    ToastMessage(viewModel.export.toast)
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
