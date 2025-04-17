package com.feko.generictabletoprpg.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.theme.Typography

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDialogDismissed: () -> Unit,
    @StringRes
    dialogTitle: Int = R.string.delete_dialog_title,
    @StringRes
    dialogMessage: Int? = null
) {
    BasicAlertDialog(onDismissRequest = onDialogDismissed) {
        Card {
            Column(Modifier.padding(start = 16.dp, top = 16.dp, end = 8.dp)) {
                DialogTitle(dialogTitle)
                if (dialogMessage != null) {
                    Text(stringResource(dialogMessage), Modifier.padding(top = 8.dp))
                }
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.End
                ) {
                    TextButton(onClick = onDialogDismissed) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(onClick = {
                        onConfirm()
                        onDialogDismissed()
                    }) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <T> SelectFromListDialog(
    @StringRes
    dialogTitle: Int,
    listItems: List<T>,
    getListItemKey: ((item: T) -> Any)?,
    onItemSelected: (T) -> Unit,
    onDialogDismissed: () -> Unit,
    listItem: @Composable (T) -> Unit
) {
    BasicAlertDialog(onDialogDismissed) {
        Card {
            Column(Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)) {
                DialogTitle(dialogTitle)
                LazyColumn(
                    Modifier.padding(top = 16.dp, bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listItems, getListItemKey) {
                        Box(Modifier.clickable {
                            onItemSelected(it)
                            onDialogDismissed()
                        }) {
                            listItem(it)
                        }
                    }
                }
                TextButton(
                    onClick = onDialogDismissed,
                    Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

@Composable
fun DialogTitle(dialogTitle: String) {
    Text(dialogTitle, style = Typography.titleLarge)
}

@Composable
fun DialogTitle(@StringRes dialogTitleResource: Int) {
    DialogTitle(stringResource(dialogTitleResource))
}