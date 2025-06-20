package com.feko.generictabletoprpg.features.spell.ui

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
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.features.spell.Spell


@Composable
fun SpellDetailsAppBarSetup(appViewModel: AppViewModel) {
    appViewModel.set(
        appBarTitle = stringResource(R.string.spell_details_title),
        navBarActions = listOf()
    )
}

@Composable
fun SpellDetailsContent(
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
