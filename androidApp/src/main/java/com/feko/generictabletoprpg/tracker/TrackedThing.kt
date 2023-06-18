package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.common.OverviewScreen
import com.feko.generictabletoprpg.theme.Typography
import com.feko.generictabletoprpg.tracker.TrackedThing
import org.koin.androidx.compose.koinViewModel


object Tracker : OverviewScreen<TrackerViewModel, TrackedThing>() {
    override val screenTitle: String
        get() = "Tracker"
    override val route: String
        get() = "tracker"
    override val isRootDestination: Boolean
        get() = true
    override val detailsNavRouteProvider: Navigation.DetailsNavRouteProvider
        get() = object : Navigation.DetailsNavRouteProvider {
            override fun getNavRoute(id: Long): String {
                throw UnsupportedOperationException(
                    "Details nav route provider should not be used from the search all screen."
                )
            }
        }
    override val isFabEnabled: Boolean
        get() = true
    override val isFabDropdownMenuEnabled: Boolean
        get() = true

    @Composable
    override fun getViewModel(): TrackerViewModel = koinViewModel()

    @Composable
    override fun DropdownMenuContent(viewModel: TrackerViewModel) {
        TrackedThing.Type
            .values()
            .drop(1) // None is dropped
            .forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        viewModel.showDialog(type)
                    })
            }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun AlertDialogComposable(viewModel: TrackerViewModel) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDialog() },
            properties = DialogProperties()
        ) {
            Card {
                EditDialog(viewModel)
            }
        }
    }

    @Composable
    private fun EditDialog(viewModel: TrackerViewModel) {
        Column(
            Modifier.padding(16.dp),
            Arrangement.spacedBy(16.dp)
        ) {
            val type by viewModel.editedTrackedThingType.collectAsState()
            Text(type.name, style = Typography.titleLarge)
            NameTextField(viewModel)
            SpellSlotLevelTextField(type, viewModel)
            ValueTextField(viewModel, type)
            val buttonEnabled by viewModel.confirmButtonEnabled.collectAsState()
            TextButton(
                onClick = { viewModel.validateAndCreateTrackedThing() },
                enabled = buttonEnabled,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(16.dp)
                    .align(Alignment.End)
            ) {
                Text("Confirm")
            }
        }
    }

    @Composable
    private fun NameTextField(viewModel: TrackerViewModel) {
        val focusRequester = remember { FocusRequester() }
        val nameInputData by viewModel.editedTrackedThingName.collectAsState()
        val focusManager = LocalFocusManager.current
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

    @Composable
    private fun SpellSlotLevelTextField(
        type: TrackedThing.Type,
        viewModel: TrackerViewModel
    ) {
        if (type == TrackedThing.Type.SpellSlot) {
            val spellSlotLevelInputData
                    by viewModel.editedTrackedThingSpellSlotLevel.collectAsState()
            TextField(
                value = spellSlotLevelInputData.value,
                onValueChange = { viewModel.setLevel(it) },
                isError = !spellSlotLevelInputData.isValid,
                label = {
                    Text(
                        "Level",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel.setLevel("") }
                    ) {
                        Icon(Icons.Default.Clear, "")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
        }
    }

    @Composable
    private fun ValueTextField(
        viewModel: TrackerViewModel,
        type: TrackedThing.Type
    ) {
        val valueInputData by viewModel.editedTrackedThingValue.collectAsState()
        TextField(
            value = valueInputData.value,
            onValueChange = { viewModel.setValue(it) },
            isError = !valueInputData.isValid,
            suffix = {
                if (type == TrackedThing.Type.Percentage) {
                    Text("%")
                }
            },
            label = {
                Text(
                    "Amount",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { viewModel.setValue("") }
                ) {
                    Icon(Icons.Default.Clear, "")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { viewModel.validateAndCreateTrackedThing() }
            )
        )
    }
}