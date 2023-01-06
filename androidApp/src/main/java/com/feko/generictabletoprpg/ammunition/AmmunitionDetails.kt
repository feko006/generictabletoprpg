package com.feko.generictabletoprpg.ammunition

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.feko.generictabletoprpg.common.Common.TextWithLabel
import com.feko.generictabletoprpg.common.DetailsScreen
import com.feko.generictabletoprpg.common.DetailsViewModel
import org.koin.androidx.compose.koinViewModel

object AmmunitionDetails : DetailsScreen<AmmunitionDetailsViewModel, Ammunition>() {
    override val screenTitle: String
        get() = "Ammunition Details"
    override val isRootDestination: Boolean
        get() = false
    override val routeBase: String
        get() = "ammunitionDetails"

    @Composable
    override fun ScreenContent(
        readiedItem: DetailsViewModel.DetailsScreenState.ItemReady<Ammunition>,
        padding: Dp
    ) {
        readiedItem.item.run {
            TextWithLabel("Name", name)
            TextWithLabel("Sell quantity", sellQuantity.toString())
            TextWithLabel("Cost", cost.toString())
            TextWithLabel("Weight", weight)
        }
    }

    @Composable
    override fun getViewModel(): AmmunitionDetailsViewModel = koinViewModel()
}