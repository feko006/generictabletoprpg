package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

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
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.DialogTitle
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.OverviewScreen
import com.feko.generictabletoprpg.common.Named
import com.feko.generictabletoprpg.tracker.TrackedThingGroup
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackerGroupsScreen(
    navHostController: NavHostController,
    appViewModel: AppViewModel,
    getTrackerNavRoute: (Long) -> String
) {
    val viewModel: TrackerGroupViewModel = koinViewModel()
    appViewModel
        .set(
            appBarTitle = "Tracker",
            navBarActions = listOf()
        )
    OverviewScreen(
        navController = navHostController,
        viewModel = viewModel,
        listItem = { item, navController ->
            OverviewListItem(
                item = item,
                navController = navController,
                viewModel = viewModel,
                getNavRoute = getTrackerNavRoute
            )
        },
        isFabEnabled = true,
        onFabClicked = {
            viewModel.newTrackedThingGroupRequested()
        },
        alertDialogComposable = {
            AlertDialogComposable(viewModel)
        }
    )
}

@Composable
fun OverviewListItem(
    item: TrackedThingGroup,
    navController: NavHostController,
    viewModel: TrackerGroupViewModel,
    getNavRoute: (Long) -> String
) {
    ListItem(
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text((item as Named).name, modifier = Modifier.weight(1f))
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
                navController.navigate(getNavRoute(item.id))
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
                DialogTitle(viewModel.dialogTitle)
                val focusRequester = remember { FocusRequester() }
                val nameInputData by viewModel.groupName.collectAsState()
                if (viewModel.dialogType == TrackerGroupViewModel.DialogType.NewOrUpdate) {
                    TextField(
                        value = nameInputData.value,
                        onValueChange = { viewModel.setName(it) },
                        isError = !nameInputData.isValid,
                        label = {
                            Text(
                                "Name",
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
                            Text("Cancel")
                        }
                    }
                    val buttonEnabled by viewModel.confirmButtonEnabled.collectAsState()
                    TextButton(
                        onClick = { viewModel.confirmDialogAction() },
                        enabled = buttonEnabled,
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}