package com.feko.generictabletoprpg.common.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
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
        OutlinedTextField(
            textFieldValue,
            onValueChange = {},
            Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
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
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
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
