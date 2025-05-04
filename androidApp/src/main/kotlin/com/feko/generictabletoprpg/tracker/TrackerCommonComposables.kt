package com.feko.generictabletoprpg.tracker

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

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
            Icon(Icons.Default.Edit, "")
        }
        IconButton(onClick = onDeleteButtonClicked) {
            Icon(Icons.Default.Delete, "")
        }
    }
}

@Composable
fun DropdownMenuContent(onTrackedThingClicked: (TrackedThing.Type, Context) -> Unit) {
    val context = LocalContext.current
    TrackedThing.Type
        .entries
        .drop(1) // None is dropped
        .sortedBy { it.name }
        .forEach { type ->
            DropdownMenuItem(
                text = { Text(type.name) },
                onClick = { onTrackedThingClicked(type, context) },
                modifier = Modifier.widthIn(min = 200.dp)
            )
        }
}
