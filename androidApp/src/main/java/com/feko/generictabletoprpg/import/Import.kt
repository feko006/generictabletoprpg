package com.feko.generictabletoprpg.import

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.RootDestinations
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun ImportScreen(
    appViewModel: AppViewModel
) {
    val viewModel: ImportViewModel = koinViewModel()
    appViewModel.run {
        set(
            appBarTitle = stringResource(R.string.import_title),
            navBarActions = listOf()
        )
        updateActiveDrawerItem(RootDestinations.Import)
    }
    Box(
        Modifier.fillMaxSize(),
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
                            .use {
                                it.readText()
                            }
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
            is ImportViewModel.ImportScreenState.ReadyToImport -> {
                FloatingActionButton(
                    onClick = {
                        pickFileLauncher.launch("*/*")
                    }
                ) {
                    Icon(Icons.Default.Add, "")
                }
            }

            is ImportViewModel.ImportScreenState.Importing -> {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}