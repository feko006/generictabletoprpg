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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.dismiss
import com.feko.generictabletoprpg.shared.common.ui.components.AlertDialogBase
import com.feko.generictabletoprpg.shared.common.ui.components.BoxWithScrollIndicator
import com.feko.generictabletoprpg.shared.common.ui.components.DialogTitle
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.features.tracker.model.IEquipmentItem
import org.jetbrains.compose.resources.stringResource

@Composable
fun EquipmentListDialog(
    dialog: ITrackerDialog.EquipmentListDialog,
    onEquipmentClick: (IEquipmentItem) -> Unit,
    onDismiss: () -> Unit
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
        EquipmentListContent(dialog, onEquipmentClick)
    }
}

@Composable
fun ColumnScope.EquipmentListContent(
    dialog: ITrackerDialog.EquipmentListDialog,
    onEquipmentClick: (IEquipmentItem) -> Unit
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
                dialog.equipmentContainer.entries,
                key = { it.item.name }
            ) {
                EquipmentListItem(
                    it.item.name,
                    it.count,
                    onClick = { onEquipmentClick(it.item) },
                    {},
                    {})
            }
        }
    }
}

@Composable
private fun EquipmentListItem(
    name: String,
    count: Int,
    onClick: () -> Unit,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
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
            IconButton(onDecrement) { Text("<") }
            Text(count.toString())
            IconButton(onIncrement) { Text(">") }
        }
    }
}