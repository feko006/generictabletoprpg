package com.feko.generictabletoprpg.spell

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.DetailsScreen
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.Common.TextWithLabel
import org.koin.androidx.compose.koinViewModel


object SpellDetails : DetailsScreen<SpellDetailsViewModel, Spell>() {
    override val routeBase = "spellDetails"
    override val isRootDestination: Boolean
        get() = false
    override val screenTitle: String
        get() = "Spell Details"

    @Composable
    override fun getViewModel(): SpellDetailsViewModel = koinViewModel()

    @Composable
    override fun ScreenContent(
        readiedFeat: DetailsViewModel.DetailsScreenState.ItemReady<Spell>,
        padding: Dp
    ) {
        readiedFeat.item.run {
            TextWithLabel("Name", name)
            Spacer(Modifier.height(padding))
            TextWithLabel("Level", level.toString())
            Spacer(Modifier.height(padding))
            TextWithLabel("School", school)
            Spacer(Modifier.height(padding))
            TextWithLabel("Casting time", castingTime)
            Spacer(Modifier.height(padding))
            TextWithLabel("Range", range.toString())
            if (hasComponents) {
                Spacer(Modifier.height(padding))
                TextWithLabel("Components", components.toString())
            }
            Spacer(Modifier.height(padding))
            TextWithLabel("Duration", duration)
            Divider(Modifier.padding(vertical = padding))
            Text(description)
        }
    }
}