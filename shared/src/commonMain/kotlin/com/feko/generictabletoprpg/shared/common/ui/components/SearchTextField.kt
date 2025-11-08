package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.search
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import kotlinx.coroutines.delay

@Composable
fun SearchTextField(
    initialSearchString: String,
    onValueChange: (String) -> Unit,
    hint: IText = Res.string.search.asText(),
    debounceMs: Long = 500
) {
    var searchString by remember(initialSearchString) { mutableStateOf(initialSearchString) }
    LaunchedEffect(searchString) {
        if (searchString.isNotEmpty()) {
            delay(debounceMs)
            onValueChange(searchString)
        }
    }
    val donePressed = remember { mutableStateOf(false) }
    GttrpgTextField(
        value = searchString,
        onValueChange = {
            searchString = it
        },
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                hint.text(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        },
        leadingIcon = { Icon(searchIcon, "") },
        trailingIcon = {
            if (searchString.isNotEmpty()) {
                IconButton(
                    onClick = { onValueChange("") }
                ) {
                    Icon(clearIcon, "")
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            donePressed.value = true
        })
    )
    if (donePressed.value) {
        LocalFocusManager.current.clearFocus()
        donePressed.value = false
    }
}