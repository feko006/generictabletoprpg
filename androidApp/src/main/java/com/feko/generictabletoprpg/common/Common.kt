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
}