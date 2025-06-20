package com.feko.generictabletoprpg.common.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.theme.Typography

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AlertDialogBase(
    onDialogDismiss: () -> Unit,
    screenHeight: Float = 1f,
    dialogTitle: @Composable ColumnScope.() -> Unit = {},
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    dialogButtons: @Composable (RowScope.() -> Unit)? = null,
    dialogContent: @Composable ColumnScope.() -> Unit = {}
) {
    BasicAlertDialog(onDismissRequest = onDialogDismiss) {
        Card {
            Column(
                Modifier
                    .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * screenHeight)
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                verticalArrangement
            ) {
                dialogTitle()
                dialogContent()
                if (dialogButtons != null) {
                    Row(
                        Modifier.fillMaxWidth(),
                        Arrangement.End
                    ) {
                        dialogButtons()
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDialogDismiss: () -> Unit,
    dialogTitle: String,
    dialogMessage: String? = null
) {
    AlertDialogBase(
        onDialogDismiss,
        dialogTitle = { DialogTitle(dialogTitle) },
        dialogButtons = {
            TextButton(onClick = onDialogDismiss) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(onClick = {
                onConfirm()
                onDialogDismiss()
            }) {
                Text(stringResource(R.string.confirm))
            }
        }) {
        if (dialogMessage != null) {
            Text(dialogMessage, Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun EnterValueDialog(
    onConfirm: (String) -> Unit,
    onDialogDismissed: () -> Unit,
    dialogTitle: String,
    inputFieldLabel: String = stringResource(R.string.amount),
    isInputFieldValid: (String) -> Boolean = { it.isNotEmpty() },
    canSubmitForm: (String) -> Boolean = isInputFieldValid,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    ),
    suffix: @Composable () -> Unit = {}
) {
    var value by remember { mutableStateOf("") }
    AlertDialogBase(
        onDialogDismissed,
        dialogTitle = { DialogTitle(dialogTitle) },
        dialogButtons = {
            TextButton(onClick = onDialogDismissed) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(
                onClick = {
                    onConfirm(value)
                    onDialogDismissed()
                },
                enabled = isInputFieldValid(value)
            ) {
                Text(stringResource(R.string.confirm))
            }
        }) {
        InputField(
            value,
            inputFieldLabel,
            onValueChange = { value = it },
            modifier = Modifier.padding(vertical = 8.dp),
            onFormSubmit = {
                onConfirm(value)
                onDialogDismissed()
            },
            canSubmitForm,
            isInputFieldValid,
            keyboardOptions = keyboardOptions,
            autoFocus = true,
            suffix = suffix
        )
    }
}

@Composable
fun <T> SelectFromListDialog(
    dialogTitle: String,
    listItems: List<T>,
    getListItemKey: ((item: T) -> Any)?,
    onItemSelect: (T) -> Unit,
    onDialogDismiss: () -> Unit,
    listItem: @Composable (T) -> Unit
) {
    AlertDialogBase(
        onDialogDismiss,
        dialogTitle = { DialogTitle(dialogTitle) },
        dialogButtons = {
            TextButton(onClick = onDialogDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }) {
        LazyColumn(
            Modifier.padding(top = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listItems, getListItemKey) {
                Box(Modifier.clickable {
                    onItemSelect(it)
                    onDialogDismiss()
                }) {
                    listItem(it)
                }
            }
        }
    }
}

@Composable
fun DialogTitle(dialogTitle: String) {
    Text(dialogTitle, style = Typography.titleLarge)
}