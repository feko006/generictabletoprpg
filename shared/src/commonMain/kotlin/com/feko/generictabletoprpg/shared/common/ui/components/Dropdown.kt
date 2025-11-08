package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <T> GttrpgDropdownField(
    options: List<T>,
    textFieldValue: String,
    expanded: Boolean,
    enabled: Boolean,
    onDropdownExpandedStateChanged: (Boolean) -> Unit,
    onDropdownMenuItemClick: (T) -> Unit,
    dropdownMenuItemText: @Composable (T) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onDropdownExpandedStateChanged(enabled && !expanded) },
        modifier = Modifier.fillMaxWidth()
    ) {
        var textFieldSize by remember { mutableStateOf(Size.Zero) }
        GttrpgOutlinedTextField(
            textFieldValue,
            onValueChange = {},
            Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .onGloballyPositioned { textFieldSize = it.size.toSize() },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            singleLine = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDropdownExpandedStateChanged(false) },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { dropdownMenuItemText(option) },
                    onClick = { onDropdownMenuItemClick(option) },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GttrpgContextMenu(
    expanded: Boolean,
    onDropdownExpandedStateChanged: (Boolean) -> Unit,
    dropdownMenuContent: @Composable (ColumnScope.() -> Unit)
) {
    ExposedDropdownMenuBox(
        expanded,
        onExpandedChange = { onDropdownExpandedStateChanged(!expanded) }) {
        IconButton(
            onClick = { onDropdownExpandedStateChanged(!expanded) },
            Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        ) {
            Icon(moveVertIcon, "")
        }
        ExposedDropdownMenu(
            expanded,
            onDismissRequest = { onDropdownExpandedStateChanged(false) },
            matchAnchorWidth = false,
            shape = MaterialTheme.shapes.extraLarge,
            content = dropdownMenuContent
        )
    }
}