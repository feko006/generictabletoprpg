package com.feko.generictabletoprpg.common.composable

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.action.Action
import com.feko.generictabletoprpg.ammunition.Ammunition
import com.feko.generictabletoprpg.armor.Armor
import com.feko.generictabletoprpg.common.toast.IToastSubViewModel
import com.feko.generictabletoprpg.condition.Condition
import com.feko.generictabletoprpg.disease.Disease
import com.feko.generictabletoprpg.feat.Feat
import com.feko.generictabletoprpg.spell.Spell
import com.feko.generictabletoprpg.weapon.Weapon

data class InputFieldData(
    val value: String,
    val isValid: Boolean
) {
    companion object {
        val EMPTY = InputFieldData("", true)
    }
}

@Composable
fun SearchTextField(
    searchString: String,
    onValueChange: (String) -> Unit,
    @StringRes
    hintResource: Int = R.string.search
) {
    val donePressed = remember { mutableStateOf(false) }
    TextField(
        value = searchString,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                stringResource(hintResource),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        },
        leadingIcon = { Icon(Icons.Default.Search, "") },
        trailingIcon = {
            IconButton(
                onClick = { onValueChange("") }
            ) {
                Icon(Icons.Default.Clear, "")
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            donePressed.value = true
        })
    )
    if (donePressed.value) {
        LocalFocusManager.current.clearFocus()
        donePressed.value = false
    }
}

@Composable
fun TextWithLabel(
    @StringRes labelResource: Int,
    text: String
) {
    Text(
        buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(stringResource(labelResource))
            }
            append(": $text")
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Dropdown(
    textFieldValue: String,
    dropdownExpanded: Boolean,
    enabled: Boolean,
    onDropdownExpandedStateChanged: (Boolean) -> Unit,
    dropdownMenuContent: @Composable () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = dropdownExpanded,
        onExpandedChange = { onDropdownExpandedStateChanged(enabled && !dropdownExpanded) },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            readOnly = true
        )
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { onDropdownExpandedStateChanged(false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            dropdownMenuContent()
        }
    }
}

@Composable
fun InputField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    onFormSubmit: () -> Unit = {},
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
            { Icon(Icons.Default.Clear, "") }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(focusDirection) },
            onDone = { onFormSubmit() }
        ),
        colors = colors
    )
    if (autoFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFABButtonWithDropdown(
    expanded: Boolean,
    modifier: Modifier,
    onDismissRequest: () -> Unit,
    onFabClicked: () -> Unit,
    dropdownMenuContent: @Composable () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {},
        modifier = Modifier
            .wrapContentSize()
            .then(modifier)
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            content = {
                dropdownMenuContent()
            }
        )
        AddFABButton(
            Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
        ) { onFabClicked() }
    }
}

@Composable
fun AddFABButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        Modifier
            .size(48.dp)
            .then(modifier)
    ) {
        Icon(Icons.Default.Add, "")
    }
}

val appNamesByType = mapOf(
    Action::class.java to R.string.action,
    Ammunition::class.java to R.string.ammunition,
    Armor::class.java to R.string.armor,
    Condition::class.java to R.string.condition,
    Disease::class.java to R.string.disease,
    Feat::class.java to R.string.feat,
    Spell::class.java to R.string.spell,
    Weapon::class.java to R.string.weapon
)

val appTypes = appNamesByType.keys

@Composable
fun <T> getTypeName(type: Class<T>): String = stringResource(appNamesByType[type]!!)

@Composable
fun ToastMessage(toast: IToastSubViewModel) {
    val shouldShowToastMessage by toast.shouldShowMessage.collectAsState(false)
    if (shouldShowToastMessage) {
        Toast
            .makeText(
                LocalContext.current,
                toast.getMessage(),
                Toast.LENGTH_SHORT
            )
            .show()
        toast.messageConsumed()
    }
}

@Composable
fun CheckboxWithText(
    isChecked: Boolean,
    textStringResource: Int,
    onCheckChanged: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onCheckChanged(!isChecked) }
            .padding(end = 8.dp),
        Arrangement.Start,
        Alignment.CenterVertically
    ) {
        Checkbox(
            isChecked,
            { checked -> onCheckChanged(checked) }
        )
        Text(stringResource(textStringResource))
    }
}
