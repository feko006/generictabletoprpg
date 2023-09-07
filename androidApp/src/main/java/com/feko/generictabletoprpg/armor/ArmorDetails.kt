package com.feko.generictabletoprpg.armor

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.DetailsScreen
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.TextWithLabel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ArmorDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    appViewModel.set(appBarTitle = "Armor Details", navBarActions = listOf())
    DetailsScreen<ArmorDetailsViewModel, Armor>(
        id,
        koinViewModel()
    ) { item, padding ->

        item.run {
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