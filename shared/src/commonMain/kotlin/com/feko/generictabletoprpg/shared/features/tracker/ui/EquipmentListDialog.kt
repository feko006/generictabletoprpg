package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.confirm
import com.feko.generictabletoprpg.description
import com.feko.generictabletoprpg.details
import com.feko.generictabletoprpg.dismiss
import com.feko.generictabletoprpg.edit
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.remove
import com.feko.generictabletoprpg.set_quantity
import com.feko.generictabletoprpg.shared.common.ui.components.AlertDialogBase
import com.feko.generictabletoprpg.shared.common.ui.components.BoxWithScrollIndicator
import com.feko.generictabletoprpg.shared.common.ui.components.ConfirmationDialog
import com.feko.generictabletoprpg.shared.common.ui.components.DialogButton
import com.feko.generictabletoprpg.shared.common.ui.components.DialogInputField
import com.feko.generictabletoprpg.shared.common.ui.components.DialogTitle
import com.feko.generictabletoprpg.shared.common.ui.components.EnterValueDialog
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgContextMenu
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentContainer
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentItem
import com.feko.generictabletoprpg.shared.features.tracker.model.IEquipmentItem
import org.jetbrains.compose.resources.stringResource

@Composable
fun EquipmentListDialog(
    dialog: ITrackerDialog.EquipmentListDialog,
    viewModel: TrackerViewModel,
    onEquipmentClick: (IEquipmentItem) -> Unit
) {
    val onDismiss = viewModel::dismissDialog
    AlertDialogBase(
        onDialogDismiss = onDismiss,
        screenHeight = 0.6f,
        dialogTitle = { DialogTitle(dialog.title.text()) },
        dialogButtons = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(stringResource(Res.string.dismiss))
                }
            }
        }
    ) {
        EquipmentListContent(
            dialog,
            onEquipmentClick,
            onEdit = { viewModel.showEditEquipmentItemDialog(dialog.equipment, it) },
            onSetQuantity = viewModel::setItemQuantityRequested,
            onRemove = viewModel::removeItemFromEquipmentListRequested
        )
    }
}

@Composable
fun EquipmentListSecondaryDialog(
    dialog: ITrackerDialog.EquipmentListDialog,
    viewModel: TrackerViewModel,
    onPopEquipmentListScreen: () -> Unit = {}
) {
    when (dialog.secondaryDialog) {
        IEquipmentListDialogDialogs.None -> Unit

        is IEquipmentListDialogDialogs.ConfirmItemRemovalDialog ->
            ConfirmationDialog(
                onConfirm = {
                    viewModel.removeEquipmentItemFromList(
                        dialog.equipment,
                        dialog.secondaryDialog.equipmentEntry,
                        onPopEquipmentListScreen
                    )
                },
                viewModel::dismissEquipmentListSecondaryDialog,
                dialog.secondaryDialog.title.text()
            )

        is IEquipmentListDialogDialogs.SetItemQuantityDialog ->
            EnterValueDialog(
                onConfirm = {
                    viewModel.setItemQuantity(
                        dialog.equipment,
                        dialog.secondaryDialog.equipmentEntry,
                        it
                    )
                },
                onDialogDismissed = viewModel::dismissEquipmentListSecondaryDialog,
                dialogTitle = dialog.secondaryDialog.title.text(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

        is ITrackerDialog.EditEquipmentItemDialog ->
            AddNewEquipmentItemDialog(dialog.secondaryDialog, viewModel)
    }
}

@Composable
fun EquipmentListContent(
    dialog: ITrackerDialog.EquipmentListDialog,
    onEquipmentClick: (IEquipmentItem) -> Unit,
    onEdit: (IEquipmentItem) -> Unit,
    onSetQuantity: (EquipmentEntry) -> Unit,
    onRemove: (EquipmentEntry) -> Unit
) {
    val scrollState = rememberLazyListState()
    val dimens = LocalDimens.current
    BoxWithScrollIndicator(
        scrollState,
        backgroundColor = CardDefaults.cardColors().containerColor,
        Modifier.padding(top = dimens.paddingSmall)
    ) {
        LazyColumn(
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(dimens.gapSmall)
        ) {
            items(
                (dialog.equipment.serializedItem as EquipmentContainer).entries,
                key = { it.item.name }
            ) {
                EquipmentListItem(
                    it.item.name,
                    it.count,
                    isEditMenuItemVisible = it.item is EquipmentItem,
                    onClick = { onEquipmentClick(it.item) },
                    onEdit = { onEdit(it.item) },
                    onSetQuantity = { onSetQuantity(it) },
                    onRemove = { onRemove(it) })
            }
        }
    }
}

@Composable
private fun EquipmentListItem(
    name: String,
    count: Int,
    isEditMenuItemVisible: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onSetQuantity: () -> Unit,
    onRemove: () -> Unit
) {
    Card(shape = MaterialTheme.shapes.extraLarge) {
        val dimens = LocalDimens.current
        Row(
            Modifier.clickable(onClick = onClick)
                .padding(dimens.paddingSmall)
                .padding(start = dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, modifier = Modifier.weight(1f))
            var contextMenuExpanded by remember { mutableStateOf(false) }
            if (count > 1) {
                Text("x $count")
            }
            GttrpgContextMenu(
                contextMenuExpanded,
                { contextMenuExpanded = it }
            ) {
                DropdownMenuItem(
                    { Text(stringResource(Res.string.details)) },
                    onClick = {
                        onClick()
                        contextMenuExpanded = false
                    })
                if (isEditMenuItemVisible) {
                    DropdownMenuItem(
                        { Text(stringResource(Res.string.edit)) },
                        onClick = {
                            onEdit()
                            contextMenuExpanded = false
                        })
                }
                DropdownMenuItem(
                    { Text(stringResource(Res.string.set_quantity)) },
                    onClick = {
                        onSetQuantity()
                        contextMenuExpanded = false
                    })
                DropdownMenuItem(
                    { Text(stringResource(Res.string.remove)) },
                    onClick = {
                        onRemove()
                        contextMenuExpanded = false
                    })
            }
        }
    }
}

@Composable
fun AddNewEquipmentItemDialog(
    dialog: ITrackerDialog.EditEquipmentItemDialog,
    viewModel: TrackerViewModel
) {
    var equipmentItem by remember { mutableStateOf(dialog.equipmentItem) }
    val canConfirmDialog = equipmentItem.isValid()
    val onFormSubmit = {
        viewModel.createOrEditEquipmentItem(equipmentItem)
    }
    AlertDialogBase(
        onDialogDismiss = viewModel::dismissDialog,
        dialogTitle = { DialogTitle(dialog.title.text()) },
        dialogButtons = {
            DialogButton(
                stringResource(Res.string.confirm),
                onClick = onFormSubmit,
                isEnabled = canConfirmDialog
            )
        }
    ) {
        DialogInputField(
            equipmentItem.name,
            stringResource(Res.string.name),
            onValueChange = {
                equipmentItem = equipmentItem.copy(name = it)
            },
            isInputFieldValid = { equipmentItem.isNameValid() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            autoFocus = true
        )
        DialogInputField(
            value = equipmentItem.description,
            label = stringResource(Res.string.description),
            onValueChange = {
                equipmentItem = equipmentItem.copy(description = it)
            },
            onFormSubmit = {
                if (canConfirmDialog) {
                    onFormSubmit()
                }
            },
            isInputFieldValid = { equipmentItem.isDescriptionValid() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            maxLines = 5
        )
    }
}