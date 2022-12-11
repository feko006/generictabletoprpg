package com.feko.generictabletoprpg.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object Common {
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
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
}