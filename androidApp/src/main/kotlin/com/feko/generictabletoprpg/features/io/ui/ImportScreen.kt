package com.feko.generictabletoprpg.features.io.ui

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.common.ui.RootDestinations
import com.feko.generictabletoprpg.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImportScreen(
    appViewModel: AppViewModel,
    onNavigationIconClick: () -> Unit
) {
    val viewModel: ImportViewModel = koinViewModel()
    appViewModel.updateActiveDrawerItem(RootDestinations.Import.destination)
    Scaffold(
        topBar = { GttrpgTopAppBar(R.string.import_title.asText(), onNavigationIconClick) }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            val context = LocalContext.current
            val pickFileLauncher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.GetContent()
                ) launch@{ fileUri ->
                    if (fileUri == null) {
                        return@launch
                    }
                    val contents = context
                        .contentResolver
                        .openInputStream(fileUri)
                        ?.use { inputStream ->
                            inputStream.bufferedReader()
                                .use { it.readText() }
                        }
                    viewModel.fileSelected(contents)
                }
            val screenState by viewModel.screenState.collectAsState()
            val toastMessageResource by viewModel.toastMessage.collectAsState(0)
            if (toastMessageResource != 0) {
                Toast
                    .makeText(
                        LocalContext.current,
                        toastMessageResource,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
            when (screenState) {
                is ImportViewModel.IImportScreenState.ReadyToImport -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FloatingActionButton(
                            onClick = {
                                pickFileLauncher.launch("*/*")
                            }
                        ) {
                            Icon(Icons.Default.Add, "")
                        }
                        Text(
                            stringResource(R.string.file_import_hint),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }
                }

                is ImportViewModel.IImportScreenState.Importing -> {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            Modifier
                                .size(LocalDimens.current.visualLarge)
                                .align(Alignment.Center)
                        )
                    }
                }

                is ImportViewModel.IImportScreenState.RestartApp -> {
                    LocalActivity.current?.apply {
                        finish()
                        startActivity(intent)
                    }
                }
            }
        }
    }
}