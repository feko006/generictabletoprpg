package com.feko.generictabletoprpg.spell

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.feko.generictabletoprpg.common.Common.TextWithLabel
import com.feko.generictabletoprpg.common.DetailsScreen
import com.feko.generictabletoprpg.common.DetailsViewModel
import org.koin.androidx.compose.koinViewModel


object SpellDetails
    : DetailsScreen<SpellDetailsViewModel, Spell>(
    "Spell Details",
    "spell"
) {
    @Composable
    override fun getViewModel(): SpellDetailsViewModel = koinViewModel()

    @Composable
    override fun ScreenContent(
        readiedItem: DetailsViewModel.DetailsScreenState.ItemReady<Spell>,
        padding: Dp
    ) {
        readiedItem.item.run {
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