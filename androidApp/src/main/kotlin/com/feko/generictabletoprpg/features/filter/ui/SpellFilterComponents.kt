package com.feko.generictabletoprpg.features.filter.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.common.ui.components.CheckboxWithText
import com.feko.generictabletoprpg.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.concentration
import com.feko.generictabletoprpg.material
import com.feko.generictabletoprpg.ritual
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringText.Companion.asText
import com.feko.generictabletoprpg.shared.features.filter.Filter
import com.feko.generictabletoprpg.shared.features.filter.SpellFilter
import com.feko.generictabletoprpg.somatic
import com.feko.generictabletoprpg.verbal
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpellFilters(
    filter: SpellFilter,
    onFilterUpdate: (Filter) -> Unit
) {
    val spellFilterViewModel = koinViewModel<SpellFilterViewModel>()

    val classes by spellFilterViewModel.classes.collectAsState(listOf())
    val tabTitles = remember(classes) {
        listOf(
            R.string.common,
            R.string.school,
            R.string.level
        ).run {
            if (classes.isNotEmpty()) plus(R.string.class_term)
            else this
        }
    }
    var selectedTab by remember { mutableIntStateOf(0) }
    PrimaryScrollableTabRow(selectedTab) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selectedTab == index,
                onClick = { selectedTab = index },
                text = { Text(text = stringResource(tabTitles[index])) }
            )
        }
    }

    Crossfade(selectedTab) { selectedTab ->
        when (selectedTab) {
            tabTitles.indexOf(R.string.common) -> CommonSpellFilterSection(filter, onFilterUpdate)

            tabTitles.indexOf(R.string.school) ->
                SchoolSpellFilterSection(spellFilterViewModel, filter, onFilterUpdate)

            tabTitles.indexOf(R.string.level) ->
                LevelSpellFilterSection(spellFilterViewModel, filter, onFilterUpdate)

            tabTitles.indexOf(R.string.class_term) ->
                ClassSpellFilterSection(classes, filter, onFilterUpdate)
        }
    }
}

@Composable
private fun CommonSpellFilterSection(
    filter: SpellFilter,
    onFilterUpdate: (Filter) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalDimens.current.gapMedium)
    ) {
        GttrpgTriStateBooleanFilterField(
            Res.string.concentration.asText(),
            filter.concentration,
            Modifier.fillMaxWidth()
        ) { onFilterUpdate(filter.copyWithNewConcentration(it)) }
        GttrpgTriStateBooleanFilterField(
            Res.string.ritual.asText(),
            filter.isRitual,
            Modifier.fillMaxWidth()
        ) { onFilterUpdate(filter.copyWithNewRitual(it)) }

        Text(stringResource(R.string.components))

        GttrpgTriStateBooleanFilterField(
            Res.string.verbal.asText(),
            filter.spellComponents.verbal,
            Modifier.fillMaxWidth()
        ) {
            onFilterUpdate(
                filter.copyWithNewSpellComponents(
                    filter.spellComponents.copy(verbal = it)
                )
            )
        }

        GttrpgTriStateBooleanFilterField(
            Res.string.somatic.asText(),
            filter.spellComponents.somatic,
            Modifier.fillMaxWidth()
        ) {
            onFilterUpdate(
                filter.copyWithNewSpellComponents(
                    filter.spellComponents.copy(somatic = it)
                )
            )
        }

        GttrpgTriStateBooleanFilterField(
            Res.string.material.asText(),
            filter.spellComponents.material,
            Modifier.fillMaxWidth()
        ) {
            onFilterUpdate(
                filter.copyWithNewSpellComponents(
                    filter.spellComponents.copy(material = it)
                )
            )
        }
    }
}

@Composable
private fun SchoolSpellFilterSection(
    spellFilterViewModel: SpellFilterViewModel,
    filter: SpellFilter,
    onFilterUpdate: (Filter) -> Unit
) {
    val schools by spellFilterViewModel.schools.collectAsState(listOf())
    Column {
        schools.forEach { school ->
            CheckboxWithText(
                filter.schools.contains(school),
                school.asText()
            ) { isChecked ->
                onFilterUpdate(
                    filter.copy(
                        schools =
                            if (isChecked) {
                                filter.schools.plus(school)
                            } else {
                                filter.schools.minus(school)
                            }
                    )
                )
            }
        }
    }
}

@Composable
private fun LevelSpellFilterSection(
    spellFilterViewModel: SpellFilterViewModel,
    filter: SpellFilter,
    onFilterUpdate: (Filter) -> Unit
) {
    val levels by spellFilterViewModel.levels.collectAsState(listOf())
    Column {
        levels.forEach { level ->
            CheckboxWithText(
                filter.levels.contains(level),
                level.toString().asText()
            ) { isChecked ->
                onFilterUpdate(
                    filter.copy(
                        levels = if (isChecked) {
                            filter.levels.plus(level)
                        } else {
                            filter.levels.minus(level)
                        }
                    )
                )
            }
        }
    }
}

@Composable
private fun ClassSpellFilterSection(
    classes: List<String>,
    filter: SpellFilter,
    onFilterUpdate: (Filter) -> Unit
) {
    Column {
        Text(
            stringResource(R.string.class_filter_disclaimer),
            Modifier.padding(horizontal = LocalDimens.current.paddingMedium),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            style = MaterialTheme.typography.labelMedium
        )
        classes.forEach { clazz ->
            CheckboxWithText(
                filter.classes.contains(clazz),
                clazz.asText()
            ) { isChecked ->
                onFilterUpdate(
                    filter.copy(
                        classes = if (isChecked) {
                            filter.classes.plus(clazz)
                        } else {
                            filter.classes.minus(clazz)
                        }
                    )
                )
            }
        }
    }
}

