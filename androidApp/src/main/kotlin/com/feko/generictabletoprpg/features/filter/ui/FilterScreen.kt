package com.feko.generictabletoprpg.features.filter.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.components.GttrpgDropdownField
import com.feko.generictabletoprpg.common.ui.components.appNamesByType
import com.feko.generictabletoprpg.common.ui.components.appTypes
import com.feko.generictabletoprpg.common.ui.components.getTypeName
import com.feko.generictabletoprpg.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.features.filter.Filter
import com.feko.generictabletoprpg.features.filter.GenericFilter
import com.feko.generictabletoprpg.features.filter.SpellFilter
import com.feko.generictabletoprpg.features.spell.Spell

@Composable
fun FilterScreen(
    initialFilter: Filter?,
    isTypeFixed: Boolean = false,
    onFilterUpdated: (Filter?) -> Unit,
    onFilterCleared: () -> Unit
) {
    var filter by remember { mutableStateOf(initialFilter) }

    @StringRes
    val textFieldInitialValue by remember {
        derivedStateOf {
            filter?.let {
                appNamesByType[it.type]
            } ?: R.string.filter_type_placeholder
        }
    }

    var dropdownExpanded by remember { mutableStateOf(false) }
    val dimens = LocalDimens.current
    Column(
        Modifier.padding(dimens.paddingMedium),
        Arrangement.spacedBy(dimens.gapMedium)
    ) {
        Text(
            stringResource(R.string.filter),
            Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge
        )
        GttrpgDropdownField(
            appTypes.toList(),
            stringResource(textFieldInitialValue),
            dropdownExpanded,
            enabled = !isTypeFixed,
            onDropdownExpandedStateChanged = { dropdownExpanded = it },
            onDropdownMenuItemClick = onClick@{
                if (it == filter?.type) {
                    return@onClick
                }
                dropdownExpanded = false
                val newFilter =
                    when (it) {
                        Spell::class.java -> SpellFilter()
                        else -> GenericFilter(it)
                    }
                filter = newFilter
            }
        ) { Text(getTypeName(it)) }
        filter.let {
            if (it is SpellFilter) {
                SpellFilters(it) { updatedFilter -> filter = updatedFilter }
            }
        }
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(LocalDimens.current.gapMedium)) {
            OutlinedButton(
                onClick = onFilterCleared,
                Modifier.weight(1f)
            ) { Text(stringResource(R.string.clear_all)) }
            Button(
                onClick = { onFilterUpdated(filter) },
                Modifier.weight(1f)
            ) { Text(stringResource(R.string.apply)) }
        }
    }
}
