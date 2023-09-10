package com.feko.generictabletoprpg.spell

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.AppViewModel
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
    appViewModel.set(appBarTitle = "Spell Details", navBarActions = listOf())
    DetailsScreen<SpellDetailsViewModel, Spell>(
        id,
        koinViewModel()
    ) { item, padding ->
        item.run {
            TextWithLabel("Name", name)
            Spacer(Modifier.height(padding))
            TextWithLabel("Level", level.toString())
            Spacer(Modifier.height(padding))
            TextWithLabel("School", school)
            Spacer(Modifier.height(padding))
            TextWithLabel("Casting time", castingTimeWithRitualTag)
            Spacer(Modifier.height(padding))
            TextWithLabel("Range", range.toString())
            if (hasComponents) {
                Spacer(Modifier.height(padding))
                TextWithLabel("Components", components.toString())
            }
            Spacer(Modifier.height(padding))
            TextWithLabel("Duration", duration)
            HorizontalDivider(thickness = padding)
            Text(description)
        }
    }
}