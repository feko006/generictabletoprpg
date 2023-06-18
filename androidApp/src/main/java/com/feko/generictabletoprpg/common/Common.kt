package com.feko.generictabletoprpg.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

object Common {
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
        onValueChange: (String) -> Unit
    ) {
        TextField(
            value = searchString,
            onValueChange = onValueChange,
            label = {
                Text(
                    "Search",
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
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun TextWithLabel(
        label: String,
        text: String
    ) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(label)
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
}