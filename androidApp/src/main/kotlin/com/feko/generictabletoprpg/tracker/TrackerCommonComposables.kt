package com.feko.generictabletoprpg.tracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.tracker.actions.IBasicActionsTrackerViewModel
import com.feko.generictabletoprpg.tracker.dialogs.ICreateDialogTrackerViewModel
import com.feko.generictabletoprpg.tracker.dialogs.IDialogTrackerViewModel

@Composable
fun ItemActionsBase(
    item: TrackedThing,
    viewModel: IBasicActionsTrackerViewModel,
    actions: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        actions()
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
fun CancelButton(viewModel: IDialogTrackerViewModel) {
    TextButton(
        onClick = { viewModel.alertDialog.dismiss() },
        modifier = Modifier.wrapContentWidth()
    ) {
        Text(stringResource(R.string.cancel))
    }
}

@Composable
fun DropdownMenuContent(viewModel: ICreateDialogTrackerViewModel) {
    val context = LocalContext.current
    TrackedThing.Type
        .entries
        .drop(1) // None is dropped
        .sortedBy { it.name }
        .forEach { type ->
            DropdownMenuItem(
                text = { Text(type.name) },
                onClick = {
                    viewModel.showCreateDialog(type, context)
                },
                modifier = Modifier.widthIn(min = 200.dp)
            )
        }
}
