package com.feko.generictabletoprpg.spell

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.DetailsScreen
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.TextWithLabel
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun SpellDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    appViewModel.set(
        appBarTitle = stringResource(R.string.spell_details_title),
        navBarActions = listOf()
    )
    DetailsScreen<SpellDetailsViewModel, Spell>(
        id,
        koinViewModel()
    ) { item, padding ->
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
            HorizontalDivider(thickness = padding)
            Text(description)
        }
    }
}