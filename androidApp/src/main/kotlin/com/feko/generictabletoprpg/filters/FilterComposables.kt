package com.feko.generictabletoprpg.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.composable.Dropdown
import com.feko.generictabletoprpg.common.composable.appTypes
import com.feko.generictabletoprpg.common.composable.getTypeName
import com.feko.generictabletoprpg.spell.Spell
import org.koin.androidx.compose.koinViewModel

@Composable
fun Filter(
    filter: Filter?,
    onFilterUpdated: (Filter) -> Unit
) {
    val textFieldInitialValue =
        if (filter == null) stringResource(R.string.filter_type_placeholder) else getTypeName(filter.type)
    var textFieldValue by remember { mutableStateOf(textFieldInitialValue) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
        if (filter is Filter.SpellFilter) {
            SpellFilterFields(filter, onFilterUpdated)
        }
    }
}

@Composable
fun SpellFilterFields(
    filter: Filter.SpellFilter,
    onFilterUpdated: (Filter) -> Unit
) {
    val spellFilterViewModel = koinViewModel<SpellFilterViewModel>()
    val schools = spellFilterViewModel.schools.collectAsState(listOf())
    val schoolTextFieldInitialValue = filter.school ?: stringResource(R.string.school)
    var schoolTextFieldValue by remember { mutableStateOf(schoolTextFieldInitialValue) }
    var schoolDropdownExpanded by remember { mutableStateOf(false) }
    Dropdown(
        schoolTextFieldValue,
        schoolDropdownExpanded,
        onDropdownExpandedStateChanged = {
            schoolDropdownExpanded = it
        }
    ) {
        schools.value.forEach { school ->
            DropdownMenuItem(
                text = { Text(school) },
                onClick = {
                    schoolTextFieldValue = school
                    schoolDropdownExpanded = false
                    onFilterUpdated(
                        filter.copy(school = school)
                    )
                }
            )
        }
    }
}

