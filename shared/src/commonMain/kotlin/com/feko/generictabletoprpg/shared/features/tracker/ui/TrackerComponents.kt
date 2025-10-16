package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.shared.common.ui.components.deleteIcon
import com.feko.generictabletoprpg.shared.common.ui.components.editIcon
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import org.jetbrains.compose.resources.stringResource

@Composable
fun ItemActionsBase(
    onEditButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    actions: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        actions()
        IconButton(onClick = onEditButtonClicked) {
            Icon(editIcon, "")
        }
        IconButton(onClick = onDeleteButtonClicked) {
            Icon(deleteIcon, "")
        }
    }
}

@Composable
fun DropdownMenuContent(onTrackedThingClicked: (TrackedThing.Type) -> Unit) {
    TrackedThing.Type
        .entries
        .drop(1) // None is dropped
        .map { Pair(it, stringResource(it.nameResource!!)) }
        .sortedBy { it.second }
        .forEach { type ->
            DropdownMenuItem(
                text = { Text(type.second) },
                onClick = { onTrackedThingClicked(type.first) },
                modifier = Modifier.widthIn(min = 200.dp)
            )
        }
}