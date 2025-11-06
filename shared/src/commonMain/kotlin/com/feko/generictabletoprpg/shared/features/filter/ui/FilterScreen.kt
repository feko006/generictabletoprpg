package com.feko.generictabletoprpg.shared.features.filter.ui

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
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.apply
import com.feko.generictabletoprpg.clear_all
import com.feko.generictabletoprpg.filter
import com.feko.generictabletoprpg.filter_type_placeholder
import com.feko.generictabletoprpg.shared.common.appNamesByType
import com.feko.generictabletoprpg.shared.common.appTypes
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgDropdownField
import com.feko.generictabletoprpg.shared.common.ui.components.getTypeName
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.features.filter.Filter
import com.feko.generictabletoprpg.shared.features.filter.GenericFilter
import com.feko.generictabletoprpg.shared.features.filter.SpellFilter
import com.feko.generictabletoprpg.shared.features.spell.Spell
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilterScreen(
    initialFilter: Filter?,
    isTypeFixed: Boolean = false,
    onFilterUpdated: (Filter?) -> Unit,
    onFilterCleared: () -> Unit
) {
    var filter by remember { mutableStateOf(initialFilter) }

    val textFieldInitialValue by remember {
        derivedStateOf {
            filter?.let {
                appNamesByType[it.type]
            } ?: Res.string.filter_type_placeholder
        }
    }

    var dropdownExpanded by remember { mutableStateOf(false) }
    val dimens = LocalDimens.current
    Column(
        Modifier.padding(dimens.paddingMedium),
        Arrangement.spacedBy(dimens.gapMedium)
    ) {
        Text(
            stringResource(Res.string.filter),
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
                        Spell::class -> SpellFilter()
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
            ) { Text(stringResource(Res.string.clear_all)) }
            Button(
                onClick = { onFilterUpdated(filter) },
                Modifier.weight(1f)
            ) { Text(stringResource(Res.string.apply)) }
        }
    }
}