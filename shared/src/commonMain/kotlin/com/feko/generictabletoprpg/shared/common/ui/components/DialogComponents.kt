package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.amount
import com.feko.generictabletoprpg.cancel
import com.feko.generictabletoprpg.confirm
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.common.ui.theme.Typography
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AlertDialogBase(
    onDialogDismiss: () -> Unit,
    screenHeight: Float = 1f,
    dialogTitle: @Composable ColumnScope.() -> Unit = {},
    dialogButtons: @Composable (ColumnScope.() -> Unit)? = null,
    dialogContent: @Composable ColumnScope.() -> Unit = {}
) {
    BasicAlertDialog(onDismissRequest = onDialogDismiss) {
        Card(shape = MaterialTheme.shapes.extraLarge) {
            Column(
                Modifier
                    .heightIn(max = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.height.toDp() } * screenHeight)
                    .padding(LocalDimens.current.paddingMedium),
                Arrangement.spacedBy(LocalDimens.current.gapSmall)
            ) {
                dialogTitle()
                dialogContent()
                if (dialogButtons != null) {
                    Column(Modifier.fillMaxWidth()) {
                        dialogButtons()
                    }
                }
            }
        }
    }
}

@Composable
fun DialogButton(text: String, onClick: () -> Unit, isEnabled: Boolean = true) {
    Button(
        onClick,
        Modifier.fillMaxWidth(),
        isEnabled,
        shape = MaterialTheme.shapes.extraLarge
    ) { Text(text) }
}

@Composable
fun OutlinedDialogButton(text: String, onClick: () -> Unit, isEnabled: Boolean = true) {
    OutlinedButton(
        onClick,
        Modifier.fillMaxWidth(),
        isEnabled,
        shape = MaterialTheme.shapes.extraLarge
    ) { Text(text) }
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
            DialogButton(
                stringResource(Res.string.confirm),
                onClick = {
                    onConfirm()
                    onDialogDismiss()
                })
            OutlinedDialogButton(stringResource(Res.string.cancel), onDialogDismiss)
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
    inputFieldLabel: String = stringResource(Res.string.amount),
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
            DialogButton(
                stringResource(Res.string.confirm),
                onClick = {
                    onConfirm(value)
                    onDialogDismissed()
                },
                isEnabled = isInputFieldValid(value)
            )
            OutlinedDialogButton(stringResource(Res.string.cancel), onDialogDismissed)
        }) {
        DialogInputField(
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
            OutlinedDialogButton(stringResource(Res.string.cancel), onDialogDismiss)
        }) {
        LazyColumn(
            Modifier.padding(top = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listItems, getListItemKey) {
                Card(
                    Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .clickable {
                            onItemSelect(it)
                            onDialogDismiss()
                        },
                    shape = MaterialTheme.shapes.extraLarge
                ) { listItem(it) }
            }
        }
    }
}

@Composable
fun DialogTitle(dialogTitle: String) {
    Text(
        dialogTitle,
        Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = Typography.titleLarge
    )
}

@Composable
fun DialogInputField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFormSubmit: () -> Unit = {},
    canSubmitForm: (String) -> Boolean = { true },
    isInputFieldValid: (String) -> Boolean = { true },
    focusRequester: FocusRequester = remember { FocusRequester() },
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    focusDirection: FocusDirection = FocusDirection.Down,
    autoFocus: Boolean = false,
    maxLines: Int = 1,
    suffix: @Composable (() -> Unit) = {},
    colors: TextFieldColors = TextFieldDefaults.colors()
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        maxLines = maxLines,
        isError = !isInputFieldValid(value),
        suffix = suffix,
        label = {
            Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        },
        trailingIcon = {
            IconButton(onClick = { onValueChange("") })
            { Icon(clearIcon, "") }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .then(modifier),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(focusDirection) },
            onDone = {
                if (canSubmitForm(value)) {
                    onFormSubmit()
                }
            }
        ),
        colors = colors
    )
    if (autoFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

sealed interface IInputFieldValueConverter<T : Number> : (String) -> T {
    data object IntInputFieldValueConverter : IInputFieldValueConverter<Int> {
        override fun invoke(inputValue: String): Int = inputValue.toIntOrNull() ?: 0
    }

    data object FloatInputFieldValueConverter : IInputFieldValueConverter<Float> {
        override fun invoke(inputValue: String): Float = inputValue.toFloatOrNull() ?: 0f
    }
}

@Composable
fun <T : Number> NumberDialogInputField(
    value: T,
    label: String,
    convertInputValue: IInputFieldValueConverter<T>,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    onFormSubmit: () -> Unit = {},
    canSubmitForm: (T) -> Boolean = { true },
    isInputFieldValid: (Number) -> Boolean = { true },
    focusRequester: FocusRequester = remember { FocusRequester() },
    focusManager: FocusManager = LocalFocusManager.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    focusDirection: FocusDirection = FocusDirection.Down,
    autoFocus: Boolean = false,
    maxLines: Int = 1,
    suffix: @Composable (() -> Unit) = {},
    colors: TextFieldColors = TextFieldDefaults.colors()
) {
    var inputValue by remember { mutableStateOf(if (value == 0) "" else value.toString()) }
    @Suppress("KotlinConstantConditions")
    if (value != 0 && value != convertInputValue(inputValue)) {
        inputValue = value.toString()
    }
    DialogInputField(
        inputValue,
        label,
        onValueChange = {
            inputValue = it
            onValueChange(convertInputValue(it))
        },
        modifier,
        onFormSubmit,
        canSubmitForm = { canSubmitForm(convertInputValue(it)) },
        isInputFieldValid = { isInputFieldValid(convertInputValue(it)) },
        focusRequester,
        focusManager,
        keyboardOptions,
        focusDirection,
        autoFocus,
        maxLines,
        suffix,
        colors
    )
}