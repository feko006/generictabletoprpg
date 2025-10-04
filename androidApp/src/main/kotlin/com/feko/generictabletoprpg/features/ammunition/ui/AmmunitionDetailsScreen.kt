package com.feko.generictabletoprpg.features.ammunition.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.ammunition_details_title
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition
import org.koin.androidx.compose.koinViewModel

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
        TextWithLabel(R.string.name, it.name)
        TextWithLabel(R.string.sell_quantity, it.sellQuantity.toString())
        TextWithLabel(R.string.cost, it.cost.toString())
        TextWithLabel(R.string.weight, it.weight)
    }
}