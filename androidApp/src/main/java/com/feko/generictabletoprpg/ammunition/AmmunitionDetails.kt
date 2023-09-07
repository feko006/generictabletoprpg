package com.feko.generictabletoprpg.ammunition

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.DetailsScreen
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.TextWithLabel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AmmunitionDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    appViewModel.set(appBarTitle = "Ammunition Details", navBarActions = listOf())
    DetailsScreen<AmmunitionDetailsViewModel, Ammunition>(
        id,
        koinViewModel()
    ) { item, padding ->
        item.run {
            TextWithLabel("Name", name)
            Spacer(Modifier.height(padding))
            TextWithLabel("Sell quantity", sellQuantity.toString())
            Spacer(Modifier.height(padding))
            TextWithLabel("Cost", cost.toString())
            Spacer(Modifier.height(padding))
            TextWithLabel("Weight", weight)
        }
    }
}