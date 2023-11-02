package com.feko.generictabletoprpg.filters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
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
                                Spell::class.java -> SpellFilter()
                                else -> GenericFilter(it)
                            }
                        onFilterUpdated(newFilter)
                    }
                )
            }
        }
        if (filter is SpellFilter) {
            SpellFilterFields(filter, onFilterUpdated)
        }
    }
}

@Composable
fun SpellFilterFields(
    filter: SpellFilter,
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
                    onFilterUpdated(filter.copy(school = school))
                }
            )
        }
    }
    BooleanFilterField(
        stringResource(R.string.concentration),
        filter.concentration
    ) { onFilterUpdated(filter.copy(concentration = it)) }
}

@Composable
fun BooleanFilterField(
    name: String,
    value: Boolean?,
    onValueChanged: (Boolean?) -> Unit
) {
    val checkboxState =
        when (value) {
            null -> ToggleableState.Indeterminate
            true -> ToggleableState.On
            false -> ToggleableState.Off
        }
    val advanceState = {
        when (checkboxState) {
            ToggleableState.On -> onValueChanged(false)
            ToggleableState.Off -> onValueChanged(null)
            ToggleableState.Indeterminate -> onValueChanged(true)
        }
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { advanceState() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name)
        TriStateCheckbox(
            state = checkboxState,
            onClick = { advanceState() }
        )
    }
}

