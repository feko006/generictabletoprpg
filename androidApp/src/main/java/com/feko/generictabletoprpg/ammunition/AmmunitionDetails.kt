package com.feko.generictabletoprpg.ammunition

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.feko.generictabletoprpg.common.Common.TextWithLabel
import com.feko.generictabletoprpg.common.DetailsScreen
import com.feko.generictabletoprpg.common.DetailsViewModel
import org.koin.androidx.compose.koinViewModel

object AmmunitionDetails :
    DetailsScreen<AmmunitionDetailsViewModel, Ammunition>(
        "Ammunition Details",
        "ammunition"
    ) {
    @Composable
    override fun ScreenContent(
        readiedItem: DetailsViewModel.DetailsScreenState.ItemReady<Ammunition>,
        padding: Dp
    ) {
        readiedItem.item.run {
            TextWithLabel("Name", name)
            Spacer(Modifier.height(padding))
            TextWithLabel("Sell quantity", sellQuantity.toString())
            Spacer(Modifier.height(padding))
            TextWithLabel("Cost", cost.toString())
            Spacer(Modifier.height(padding))
            TextWithLabel("Weight", weight)
        }
    }

    @Composable
    override fun getViewModel(): AmmunitionDetailsViewModel = koinViewModel()
}