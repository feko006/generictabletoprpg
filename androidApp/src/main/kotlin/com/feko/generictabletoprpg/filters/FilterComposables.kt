package com.feko.generictabletoprpg.filters

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.composable.Dropdown
import com.feko.generictabletoprpg.common.composable.appTypes
import com.feko.generictabletoprpg.common.composable.getTypeName
import com.feko.generictabletoprpg.spell.Spell

@Composable
fun Filter(
    filter: Filter?,
    onFilterUpdated: (Filter) -> Unit
) {
    val textFieldPlaceholder = stringResource(R.string.filter_type_placeholder)
    var textFieldValue by remember { mutableStateOf(textFieldPlaceholder) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    Dropdown(
        textFieldValue,
        dropdownExpanded,
        onDropdownExpandedStateChanged = { dropdownExpanded = it }
    ) {
        appTypes.forEach {
            val typeName = getTypeName(it)
            DropdownMenuItem(
                text = {
                    Text(typeName)
                },
                onClick = onClick@{
                    if (textFieldValue == typeName) {
                        return@onClick
                    }
                    textFieldValue = typeName
                    dropdownExpanded = false
                    val newFilter =
                        when (it) {
                            Spell::class.java -> Filter.SpellFilter()
                            else -> Filter.GenericFilter(it)
                        }
                    onFilterUpdated(newFilter)
                }
            )
        }
    }
}