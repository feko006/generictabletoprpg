package com.feko.generictabletoprpg.shared.features.ammunition.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.cost
import com.feko.generictabletoprpg.sell_quantity
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition
import com.feko.generictabletoprpg.weight
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AmmunitionDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<AmmunitionDetailsViewModel, Ammunition>(
        id,
        koinViewModel(),
        onNavigationIconClick
    ) {
        AmmunitionDetailsContent(it)
    }
}

@Composable
fun AmmunitionDetailsContent(ammunition: Ammunition) {
    TextWithLabel(Res.string.sell_quantity, ammunition.sellQuantity.toString())
    TextWithLabel(Res.string.cost, ammunition.cost.toString())
    TextWithLabel(Res.string.weight, ammunition.weight)
}
