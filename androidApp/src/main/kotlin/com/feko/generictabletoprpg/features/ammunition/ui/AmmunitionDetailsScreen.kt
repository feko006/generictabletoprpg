package com.feko.generictabletoprpg.features.ammunition.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.features.ammunition.Ammunition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun AmmunitionDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<AmmunitionDetailsViewModel, Ammunition>(
        id,
        koinViewModel(),
        R.string.ammunition_details_title.asText(),
        onNavigationIconClick
    ) {
        TextWithLabel(R.string.name, it.name)
        TextWithLabel(R.string.sell_quantity, it.sellQuantity.toString())
        TextWithLabel(R.string.cost, it.cost.toString())
        TextWithLabel(R.string.weight, it.weight)
    }
}