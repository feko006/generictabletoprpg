package com.feko.generictabletoprpg.features.filters.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.components.Dropdown
import com.feko.generictabletoprpg.common.ui.components.appTypes
import com.feko.generictabletoprpg.common.ui.components.getTypeName
import com.feko.generictabletoprpg.features.filters.Filter
import com.feko.generictabletoprpg.features.filters.GenericFilter
import com.feko.generictabletoprpg.features.filters.SpellFilter
import com.feko.generictabletoprpg.features.spell.Spell
import org.koin.androidx.compose.koinViewModel

@Composable
fun Filter(
    filter: Filter?,
    isTypeFixed: Boolean = false,
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
            enabled = !isTypeFixed,
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
    StringFilterField(
        options = schools.value,
        stringResource(R.string.school),
        filter.school
    ) { value -> onFilterUpdated(filter.copy(school = value)) }

    TriStateBooleanFilterField(
        stringResource(R.string.concentration),
        filter.concentration
    ) { onFilterUpdated(filter.copyWithNewConcentration(it)) }

    val levels = spellFilterViewModel.levels.collectAsState(listOf())
    StringFilterField(
        options = levels.value,
        stringResource(R.string.level),
        filter.level?.toString()
    ) { onFilterUpdated(filter.copy(level = it.toInt())) }

    val classes = spellFilterViewModel.classes.collectAsState(listOf())
    if (classes.value.any()) {
        Column {
            StringFilterField(
                options = classes.value,
                stringResource(R.string.class_term),
                filter.`class`
            ) { onFilterUpdated(filter.copy(`class` = it)) }
            Text(
                stringResource(R.string.class_filter_disclaimer),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }

    TriStateBooleanFilterField(
        stringResource(R.string.ritual),
        filter.isRitual
    ) { onFilterUpdated(filter.copyWithNewRitual(it)) }

    MaterialComponentsFilter(filter, onFilterUpdated)
}

@Composable
private fun MaterialComponentsFilter(
    filter: SpellFilter,
    onFilterUpdated: (Filter) -> Unit
) {
    val isFilteringByComponents = filter.spellComponents != null
    BooleanFilterField(
        stringResource(R.string.components),
        isFilteringByComponents
    ) {
        onFilterUpdated(
            filter.copyWithNewSpellComponents(
                if (it) {
                    Spell.SpellComponents(
                        verbal = false,
                        somatic = false,
                        material = false,
                        materialComponent = null
                    )
                } else {
                    null
                }
            )
        )
    }
    if (isFilteringByComponents) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("V")
                Checkbox(
                    checked = filter.spellComponents!!.verbal,
                    onCheckedChange = {
                        onFilterUpdated(
                            filter.copyWithNewSpellComponents(
                                filter.spellComponents.copy(verbal = it)
                            )
                        )
                    }
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("S")
                Checkbox(
                    checked = filter.spellComponents!!.somatic,
                    onCheckedChange = {
                        onFilterUpdated(
                            filter.copyWithNewSpellComponents(
                                filter.spellComponents.copy(somatic = it)
                            )
                        )
                    }
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("M")
                Checkbox(
                    checked = filter.spellComponents!!.material,
                    onCheckedChange = {
                        onFilterUpdated(
                            filter.copyWithNewSpellComponents(
                                filter.spellComponents.copy(material = it)
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun StringFilterField(
    options: List<Any>,
    name: String,
    value: String?,
    onValueChanged: (String) -> Unit
) {
    val initialValue = value ?: name
    var textFieldValue by remember { mutableStateOf(initialValue) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    Dropdown(
        textFieldValue,
        dropdownExpanded,
        enabled = true,
        onDropdownExpandedStateChanged = {
            dropdownExpanded = it
        },
        {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = onClick@{
                        if (value == option) {
                            return@onClick
                        }
                        textFieldValue = option.toString()
                        dropdownExpanded = false
                        onValueChanged(option.toString())
                    }
                )
            }
        }
    )
}

@Composable
fun TriStateBooleanFilterField(
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

@Composable
fun BooleanFilterField(
    name: String,
    checked: Boolean,
    onValueChanged: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onValueChanged(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name)
        Checkbox(
            checked = checked,
            onCheckedChange = onValueChanged
        )
    }
}