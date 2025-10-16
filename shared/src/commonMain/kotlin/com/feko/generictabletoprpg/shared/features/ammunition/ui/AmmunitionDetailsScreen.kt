package com.feko.generictabletoprpg.shared.features.ammunition.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.ammunition_details_title
import com.feko.generictabletoprpg.cost
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.sell_quantity
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
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
        Res.string.ammunition_details_title.asText(),
        onNavigationIconClick
    ) {
        TextWithLabel(Res.string.name, it.name)
        TextWithLabel(Res.string.sell_quantity, it.sellQuantity.toString())
        TextWithLabel(Res.string.cost, it.cost.toString())
        TextWithLabel(Res.string.weight, it.weight)
    }
}