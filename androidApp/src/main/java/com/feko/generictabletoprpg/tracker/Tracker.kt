package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.feko.generictabletoprpg.ButtonState
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.OverviewScreen
import com.feko.generictabletoprpg.theme.Typography
import com.feko.generictabletoprpg.tracker.Health
import com.feko.generictabletoprpg.tracker.SpellSlot
import com.feko.generictabletoprpg.tracker.TrackedThing
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

object Tracker : OverviewScreen<TrackerViewModel, TrackedThing>(),
    Navigation.DetailsNavRouteProvider {

    private const val groupIdArgumentName = "id"
    private var groupId: Long = 0L

    override val screenTitle: String
        get() = _screenTitle
    private var _screenTitle = "Tracker"
    override val route: String
        get() = constructRoute("{$groupIdArgumentName}")
    override val isRootDestination: Boolean
        get() = false
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
    override fun getViewModel(): TrackerViewModel =
        koinViewModel(parameters = { parametersOf(groupId) })

    override fun getNavArguments(): List<NamedNavArgument> =
        listOf(navArgument(groupIdArgumentName) {
            type = NavType.LongType
        })

    override fun readNavArguments(backStackEntry: NavBackStackEntry) {
        groupId = backStackEntry.arguments!!.getLong(groupIdArgumentName)
    }

    override fun setNavBarActionsInternal(
        navBarActions: (List<ButtonState>) -> Unit,
        viewModel: TrackerViewModel
    ) {
        navBarActions(
            listOf(
                ButtonState(Icons.Default.Refresh) { viewModel.refreshAllRequested() }
            )
        )
    }

    @Composable
    public override fun OverviewListItem(item: TrackedThing, navController: NavHostController) {
        Card(Modifier.fillMaxWidth()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp)
            ) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        item.name,
                        style = Typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .padding(vertical = 4.dp)
                            .background(Color.Gray)
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(85.dp)
                    ) {
                        Text(item.getPrintableValue())
                        if (item is Health) {
                            if (item.temporaryHp > 0) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painterResource(R.drawable.shield_with_heart),
                                        "",
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(item.temporaryHp.toString(), style = Typography.bodySmall)
                                }
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) row@{
                            if (item.type == TrackedThing.Type.Percentage) {
                                return@row
                            }
                            Text(item.type.name, style = Typography.bodySmall)
                            if (item is SpellSlot) {
                                Text("Lv ${item.level}", style = Typography.bodySmall)
                            }
                        }
                    }
                }
                when (item.type) {
                    TrackedThing.Type.Percentage -> PercentageActions(item)
                    TrackedThing.Type.Number -> NumberActions(item)
                    TrackedThing.Type.Health -> HealthActions(item)
                    TrackedThing.Type.Ability -> AbilityActions(item)
                    TrackedThing.Type.SpellSlot -> SpellSlotActions(item)
                    TrackedThing.Type.None -> {}
                }
            }
        }
    }

    @Composable
    private fun PercentageActions(item: TrackedThing) {
        ItemActionsBase(item) { viewModel ->
            IconButton(
                onClick = { viewModel.addToPercentageRequested(item) },
                enabled = item.canAdd()
            ) {
                Icon(Icons.Default.Add, "")
            }
            IconButton(
                onClick = { viewModel.subtractFromPercentageRequested(item) },
                enabled = item.canSubtract()
            ) {
                Icon(painterResource(R.drawable.subtract), "")
            }
        }
    }

    @Composable
    private fun NumberActions(item: TrackedThing) {
        ItemActionsBase(item) { viewModel ->
            IconButton(
                onClick = { viewModel.addToNumberRequested(item) },
                enabled = item.canAdd()
            ) {
                Icon(Icons.Default.Add, "")
            }
            IconButton(
                onClick = { viewModel.subtractFromNumberRequested(item) },
                enabled = item.canSubtract()
            ) {
                Icon(painterResource(R.drawable.subtract), "")
            }
        }
    }

    @Composable
    private fun HealthActions(item: TrackedThing) {
        ItemActionsBase(item) { viewModel ->
            IconButton(
                onClick = { viewModel.healRequested(item) },
                enabled = item.canAdd()
            ) {
                Icon(painterResource(R.drawable.heart_plus), "")
            }
            IconButton(
                onClick = { viewModel.takeDamageRequested(item) },
                enabled = item.canSubtract()
            ) {
                Icon(painterResource(R.drawable.heart_minus), "")
            }
            IconButton(
                onClick = { viewModel.addTemporaryHp(item) },
                enabled = true
            ) {
                Icon(painterResource(R.drawable.shield_with_heart), "")
            }
            IconButton(
                onClick = { viewModel.resetValueToDefault(item) },
                enabled = item.canAdd() || (item as Health).temporaryHp > 0
            ) {
                Icon(Icons.Default.Refresh, "")
            }
        }
    }

    @Composable
    private fun AbilityActions(item: TrackedThing) {
        ItemActionsBase(item) { viewModel ->
            IconButton(
                onClick = { viewModel.useAbility(item) },
                enabled = item.canSubtract()
            ) {
                Icon(painterResource(R.drawable.subtract), "")
            }
            IconButton(
                onClick = { viewModel.resetValueToDefault(item) },
                enabled = item.canAdd()
            ) {
                Icon(Icons.Default.Refresh, "")
            }
        }
    }

    @Composable
    private fun SpellSlotActions(item: TrackedThing) {
        ItemActionsBase(item) { viewModel ->
            IconButton(
                onClick = { viewModel.useSpell(item) },
                enabled = item.canSubtract()
            ) {
                Icon(painterResource(R.drawable.subtract), "")
            }
            IconButton(
                onClick = { viewModel.resetValueToDefault(item) },
                enabled = item.canAdd()
            ) {
                Icon(Icons.Default.Refresh, "")
            }
        }
    }

    @Composable
    private fun ItemActionsBase(
        item: TrackedThing,
        actions: @Composable (TrackerViewModel) -> Unit
    ) {
        val viewModel = koinViewModel<TrackerViewModel>()
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            actions(viewModel)
            IconButton(
                onClick = { viewModel.showEditDialog(item) }
            ) {
                Icon(Icons.Default.Edit, "")
            }
            IconButton(
                onClick = { viewModel.deleteItemRequested(item) }
            ) {
                Icon(Icons.Default.Delete, "")
            }
        }
    }

    @Composable
    override fun DropdownMenuContent(viewModel: TrackerViewModel) {
        TrackedThing.Type
            .values()
            .drop(1) // None is dropped
            .forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        viewModel.showCreateDialog(type)
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
                when (viewModel.dialogType) {
                    TrackerViewModel.DialogType.Create,
                    TrackerViewModel.DialogType.Edit ->
                        EditDialog(viewModel)

                    TrackerViewModel.DialogType.ConfirmDeletion,
                    TrackerViewModel.DialogType.RefreshAll ->
                        ConfirmDialog(viewModel)

                    TrackerViewModel.DialogType.AddPercentage,
                    TrackerViewModel.DialogType.ReducePercentage ->
                        ValueInputDialog(viewModel, TrackedThing.Type.Percentage)

                    TrackerViewModel.DialogType.AddNumber,
                    TrackerViewModel.DialogType.ReduceNumber ->
                        ValueInputDialog(viewModel, TrackedThing.Type.Number)

                    TrackerViewModel.DialogType.HealHealth,
                    TrackerViewModel.DialogType.DamageHealth,
                    TrackerViewModel.DialogType.AddTemporaryHp ->
                        ValueInputDialog(viewModel, TrackedThing.Type.Health)
                }
            }
        }
    }

    @Composable
    private fun EditDialog(viewModel: TrackerViewModel) {
        DialogBase(viewModel) {
            val type by viewModel.editedTrackedThingType.collectAsState()
            NameTextField(viewModel, autoFocus = true)
            SpellSlotLevelTextField(type, viewModel)
            ValueTextField(viewModel, type) { viewModel.setValue(it) }
        }
    }

    @Composable
    private fun ConfirmDialog(viewModel: TrackerViewModel) {
        Column(
            Modifier.padding(16.dp),
            Arrangement.spacedBy(16.dp)
        ) {
            DialogTitle(viewModel)
            Row(horizontalArrangement = Arrangement.End) {
                Spacer(Modifier.weight(1f))
                TextButton(
                    onClick = { viewModel.hideDialog() },
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text("Cancel")
                }
                TextButton(
                    onClick = { viewModel.confirmDialogAction() },
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text("Confirm")
                }
            }
        }
    }

    @Composable
    private fun ValueInputDialog(
        viewModel: TrackerViewModel,
        type: TrackedThing.Type
    ) {
        DialogBase(viewModel) {
            ValueTextField(
                viewModel,
                type,
                autoFocus = true
            ) { viewModel.updateValueInputField(it) }
        }
    }

    @Composable
    private fun DialogBase(
        viewModel: TrackerViewModel,
        inputFields: @Composable () -> Unit
    ) {
        Column(
            Modifier.padding(16.dp),
            Arrangement.spacedBy(16.dp)
        ) {
            DialogTitle(viewModel)
            inputFields()
            val buttonEnabled by viewModel.confirmButtonEnabled.collectAsState()
            TextButton(
                onClick = { viewModel.confirmDialogAction() },
                enabled = buttonEnabled,
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.End)
            ) {
                Text("Confirm")
            }
        }
    }

    @Composable
    private fun NameTextField(
        viewModel: TrackerViewModel,
        autoFocus: Boolean = false
    ) {
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
        if (autoFocus) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }

    @Composable
    private fun SpellSlotLevelTextField(
        type: TrackedThing.Type,
        viewModel: TrackerViewModel
    ) {
        val focusManager = LocalFocusManager.current
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
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
        }
    }

    @Composable
    private fun ValueTextField(
        viewModel: TrackerViewModel,
        type: TrackedThing.Type,
        autoFocus: Boolean = false,
        updateValue: (String) -> Unit
    ) {
        val focusRequester = remember { FocusRequester() }
        val valueInputData by viewModel.editedTrackedThingValue.collectAsState()
        TextField(
            value = valueInputData.value,
            onValueChange = { updateValue(it) },
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
                    onClick = { updateValue("") }
                ) {
                    Icon(Icons.Default.Clear, "")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { viewModel.confirmDialogAction() }
            )
        )
        if (autoFocus) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }

    override fun getNavRoute(id: Long): String = constructRoute(id.toString())

    private fun constructRoute(id: String): String = "tracker/$id"
}