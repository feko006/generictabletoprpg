package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
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
import com.feko.generictabletoprpg.details
import com.feko.generictabletoprpg.dismiss
import com.feko.generictabletoprpg.remove
import com.feko.generictabletoprpg.set_quantity
import com.feko.generictabletoprpg.shared.common.ui.components.AlertDialogBase
import com.feko.generictabletoprpg.shared.common.ui.components.BoxWithScrollIndicator
import com.feko.generictabletoprpg.shared.common.ui.components.ConfirmationDialog
import com.feko.generictabletoprpg.shared.common.ui.components.DialogTitle
import com.feko.generictabletoprpg.shared.common.ui.components.EnterValueDialog
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgContextMenu
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentContainer
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.IEquipmentItem
import org.jetbrains.compose.resources.stringResource

@Composable
fun EquipmentListDialog(
    dialog: ITrackerDialog.EquipmentListDialog,
    onEquipmentClick: (IEquipmentItem) -> Unit,
    onDismiss: () -> Unit,
    onSetQuantityRequested: (EquipmentEntry) -> Unit,
    onRemoveRequested: (EquipmentEntry) -> Unit
) {
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
        EquipmentListContent(dialog, onEquipmentClick, onSetQuantityRequested, onRemoveRequested)
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
    }
}

@Composable
fun ColumnScope.EquipmentListContent(
    dialog: ITrackerDialog.EquipmentListDialog,
    onEquipmentClick: (IEquipmentItem) -> Unit,
    onSetQuantityRequested: (EquipmentEntry) -> Unit,
    onRemoveRequested: (EquipmentEntry) -> Unit
) {
    val scrollState = rememberLazyListState()
    val dimens = LocalDimens.current
    BoxWithScrollIndicator(
        scrollState,
        backgroundColor = CardDefaults.cardColors().containerColor,
        Modifier.weight(1f)
            .padding(top = dimens.paddingSmall)
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
                    onClick = { onEquipmentClick(it.item) },
                    onSetQuantityRequested = { onSetQuantityRequested(it) },
                    onRemoveRequested = { onRemoveRequested(it) })
            }
        }
    }
}

@Composable
private fun EquipmentListItem(
    name: String,
    count: Int,
    onClick: () -> Unit,
    onSetQuantityRequested: () -> Unit,
    onRemoveRequested: () -> Unit
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
                DropdownMenuItem(
                    { Text(stringResource(Res.string.set_quantity)) },
                    onClick = {
                        onSetQuantityRequested()
                        contextMenuExpanded = false
                    })
                DropdownMenuItem(
                    { Text(stringResource(Res.string.remove)) },
                    onClick = {
                        onRemoveRequested()
                        contextMenuExpanded = false
                    })
            }
        }
    }
}