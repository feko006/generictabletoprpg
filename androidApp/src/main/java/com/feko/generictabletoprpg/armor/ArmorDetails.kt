package com.feko.generictabletoprpg.armor

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.feko.generictabletoprpg.common.Common.TextWithLabel
import com.feko.generictabletoprpg.common.DetailsScreen
import com.feko.generictabletoprpg.common.DetailsViewModel
import org.koin.androidx.compose.koinViewModel


object ArmorDetails : DetailsScreen<ArmorDetailsViewModel, Armor>() {
    override val routeBase = "armorDetails"
    override val isRootDestination: Boolean
        get() = false
    override val screenTitle: String
        get() = "Armor Details"

    @Composable
    override fun getViewModel(): ArmorDetailsViewModel = koinViewModel()

    @Composable
    override fun ScreenContent(
        readiedItem: DetailsViewModel.DetailsScreenState.ItemReady<Armor>,
        padding: Dp
    ) {
        readiedItem.item.run {
            TextWithLabel("Name", name)
            Spacer(Modifier.height(padding))
            TextWithLabel("Type", type)
            Spacer(Modifier.height(padding))
            baseAc?.let {
                TextWithLabel("Base AC", it.toString())
                Spacer(Modifier.height(padding))
            }
            maxDexModifier?.let {
                TextWithLabel("Maximum DEX modifier", it.toString())
                Spacer(Modifier.height(padding))
            }
            stealthDisadvantage?.let {
                TextWithLabel("Stealth disadvantage", it.toString())
                Spacer(Modifier.height(padding))
            }
            weight?.let {
                TextWithLabel("Weight", weightInLbs)
                Spacer(Modifier.height(padding))
            }
            minimumStrength?.let {
                TextWithLabel("Minimum STR", minimumStrength.toString())
                Spacer(Modifier.height(padding))
            }
        }
    }
}