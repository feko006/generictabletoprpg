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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.feko.generictabletoprpg.ButtonState
import com.feko.generictabletoprpg.Navigation
import org.koin.androidx.compose.koinViewModel


object Import : Navigation.Destination {
    override val screenTitle: String
        get() = "Import"
    override val route: String
        get() = "import"
    override val isRootDestination: Boolean
        get() = true

    override fun navHostComposable(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appBarTitle: MutableState<String>,
        setNavBarActions: (List<ButtonState>) -> Unit
    ) {
        navGraphBuilder.composable(route) {
            appBarTitle.value = screenTitle
            setNavBarActions(listOf())
            Screen()
        }
    }

    @Composable
    private fun Screen(importViewModel: ImportViewModel = koinViewModel()) {
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
                    importViewModel.fileSelected(contents)
                }
            val screenState by importViewModel.screenState.collectAsState()
            val toastMessage by importViewModel.toastMessage.collectAsState("")
            if (toastMessage.isNotBlank()) {
                Toast
                    .makeText(LocalContext.current, toastMessage, Toast.LENGTH_SHORT)
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
}