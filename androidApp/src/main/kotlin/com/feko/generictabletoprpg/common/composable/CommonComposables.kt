package com.feko.generictabletoprpg.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R

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
    onDropdownExpandedStateChanged: (Boolean) -> Unit,
    dropdownMenuContent: @Composable () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = dropdownExpanded,
        onExpandedChange = { onDropdownExpandedStateChanged(!dropdownExpanded) },
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
                .menuAnchor(),
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
        AddFABButton(Modifier.menuAnchor()) { onFabClicked() }
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
