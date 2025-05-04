package com.feko.generictabletoprpg.spell

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.composable.DetailsScreen
import com.feko.generictabletoprpg.common.composable.TextWithLabel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun SpellDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    SpellDetailsAppBarSetup(appViewModel)
    DetailsScreen<SpellDetailsViewModel, Spell>(
        id,
        koinViewModel()
    ) { item, padding ->
        SpellDetailsContent(item, padding)
    }
}

@Destination<RootGraph>
@Composable
fun SimpleSpellDetailsScreen(
    spell: Spell,
    appViewModel: AppViewModel
) {
    SpellDetailsAppBarSetup(appViewModel)
    DetailsScreen(spell) { item, padding ->
        SpellDetailsContent(item, padding)
    }
}

@Composable
private fun SpellDetailsAppBarSetup(appViewModel: AppViewModel) {
    appViewModel.set(
        appBarTitle = stringResource(R.string.spell_details_title),
        navBarActions = listOf()
    )
}

@Composable
private fun SpellDetailsContent(
    item: Spell,
    padding: Dp
) {
    item.run {
        TextWithLabel(R.string.name, name)
        Spacer(Modifier.height(padding))
        TextWithLabel(R.string.level, level.toString())
        Spacer(Modifier.height(padding))
        TextWithLabel(R.string.school, school)
        Spacer(Modifier.height(padding))
        TextWithLabel(R.string.casting_time, castingTimeWithRitualTag)
        Spacer(Modifier.height(padding))
        TextWithLabel(R.string.range, range.toString())
        if (hasComponents) {
            Spacer(Modifier.height(padding))
            TextWithLabel(R.string.components, components.toString())
        }
        Spacer(Modifier.height(padding))
        TextWithLabel(R.string.duration, duration)
        if (classesThatCanCast.isNotEmpty()) {
            Spacer(Modifier.height(padding))
            TextWithLabel(R.string.classes, classesThatCanCast.joinToString())
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        Text(description)
    }
}
