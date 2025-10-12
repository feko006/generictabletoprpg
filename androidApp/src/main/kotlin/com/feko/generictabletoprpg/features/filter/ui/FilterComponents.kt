package com.feko.generictabletoprpg.features.filter.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.features.filter.Filter
import com.feko.generictabletoprpg.shared.features.filter.FilterChipData

@Composable
fun FilterChipGroup(
    filterChips: List<FilterChipData>?,
    modifier: Modifier = Modifier,
    onFilterChange: (Filter?) -> Unit
) {
    if (filterChips.isNullOrEmpty()) return
    Row(
        modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        filterChips.forEach {
            ElevatedAssistChip(
                onClick = {},
                label = { Text("${it.field.text()}: ${it.value.text()}") },
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        "",
                        Modifier.clickable { onFilterChange(it.filterOnRemove) })
                })
        }
    }
}

@Composable
fun GttrpgTriStateBooleanFilterField(
    label: IText,
    value: Boolean?,
    modifier: Modifier = Modifier,
    onValueChange: (Boolean?) -> Unit
) {
    val any = stringResource(R.string.any)
    val yes = stringResource(R.string.yes)
    val no = stringResource(R.string.no)
    val valueToOption = remember { mapOf(true to yes, false to no, null to any) }
    val optionToValue = remember { mapOf(yes to true, no to false, any to null) }
    GttrpgSingleChoiceSegmentedButtons(
        label,
        valueToOption.getValue(value),
        optionToValue.keys.toList(),
        modifier,
    ) { onValueChange(optionToValue[it]) }
}

@Composable
fun GttrpgSingleChoiceSegmentedButtons(
    label: IText,
    selectedOption: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    onOptionSelect: (String) -> Unit
) {
    Column(
        modifier,
        Arrangement.spacedBy(LocalDimens.current.gapSmall)
    ) {
        Text(label.text())
        val selectedIndex = options.indexOf(selectedOption)
        SingleChoiceSegmentedButtonRow(modifier) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    selectedIndex == index,
                    onClick = { onOptionSelect(option) },
                    shape = SegmentedButtonDefaults.itemShape(index, options.size)
                ) { Text(option) }
            }
        }
    }
}