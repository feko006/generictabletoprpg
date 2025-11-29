package com.feko.generictabletoprpg.shared.features.io.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.import_title
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.RootDestinations
import com.feko.generictabletoprpg.shared.common.ui.components.FillingLoadingIndicator
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgFloatingActionButton
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.common.ui.components.ToastMessage
import com.feko.generictabletoprpg.shared.common.ui.components.addIcon
import com.feko.generictabletoprpg.shared.common.ui.modifiers.dragAndDropTargetKmp
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.shared.fileImportHint
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ImportScreen(
    appViewModel: AppViewModel,
    onNavigationIconClick: () -> Unit
) {
    val viewModel: ImportViewModel = koinViewModel()
    appViewModel.updateActiveDrawerItem(RootDestinations.Import.destination)
    Scaffold(
        topBar = { GttrpgTopAppBar(Res.string.import_title.asText(), onNavigationIconClick) }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .dragAndDropTargetKmp(viewModel.supportedExtensions) { files ->
                    viewModel.filesSelected(files.map { PlatformFile(it) })
                },
            contentAlignment = Alignment.Center
        ) {
            val pickFileLauncher =
                rememberFilePickerLauncher(FileKitType.File(*viewModel.supportedExtensions)) { file ->
                    if (file != null) viewModel.filesSelected(listOf(file))
                }
            val toastMessage by viewModel.toastMessage.collectAsState(null)
            ToastMessage(toastMessage)
            val screenState by viewModel.screenState.collectAsState()
            when (screenState) {
                is ImportViewModel.IImportScreenState.ReadyToImport -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GttrpgFloatingActionButton(
                            onClick = { pickFileLauncher.launch() }
                        ) {
                            Icon(addIcon, "")
                        }
                        Text(
                            stringResource(fileImportHint),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }
                }

                is ImportViewModel.IImportScreenState.Importing -> FillingLoadingIndicator()
            }
        }
    }
}