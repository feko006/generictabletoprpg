package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.RootDestinations
import com.feko.generictabletoprpg.shared.common.ui.components.AddFABButton
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.common.ui.components.SearchableLazyItems
import com.feko.generictabletoprpg.shared.common.ui.components.ToastMessage
import com.feko.generictabletoprpg.shared.common.ui.components.sendToMobileIcon
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.tracker_title
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrackerGroupsScreen(
    appViewModel: AppViewModel,
    onNavigationIconClick: () -> Unit,
    onNavigateToTrackerScreen: (Long, String) -> Unit
) {
    val viewModel: TrackerGroupViewModel = koinViewModel()
    val coroutineScope = rememberCoroutineScope()
    val fileSaverLauncher =
        rememberFileSaverLauncher { file ->
            coroutineScope.launch { viewModel.onFileSaveLocationSelected(file) }
        }
    appViewModel.updateActiveDrawerItem(RootDestinations.Tracker.destination)
    Scaffold(
        topBar = {
            GttrpgTopAppBar(Res.string.tracker_title.asText(), onNavigationIconClick) {
                val exportButtonVisible by viewModel.exportButtonVisible.collectAsState(false)
                if (exportButtonVisible) {
                    IconButton(onClick = {
                        viewModel.exportAll(fileSaverLauncher)
                    }) { Icon(sendToMobileIcon, "") }
                }
            }
        },
        floatingActionButton = { AddFABButton { viewModel.newTrackedThingGroupRequested() } }
    ) { paddingValues ->
        SearchableLazyItems(
            viewModel = viewModel,
            item = { item ->
                TrackerGroupListItem(
                    item = item,
                    viewModel = viewModel,
                    fileSaverLauncher,
                    onNavigateToTrackerScreen
                )
            },
            Modifier.padding(paddingValues),
            addFabButtonSpacer = true
        )
    }
    val toastMessage by viewModel.exportToast.collectAsState(initial = null)
    ToastMessage(toastMessage)
    val dialog by viewModel.dialog.collectAsState(ITrackerGroupDialog.None)
    TrackerGroupsAlertDialog(dialog, viewModel)
}