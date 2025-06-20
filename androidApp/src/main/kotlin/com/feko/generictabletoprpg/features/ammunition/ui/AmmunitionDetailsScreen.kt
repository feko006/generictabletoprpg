package com.feko.generictabletoprpg.features.ammunition.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.features.ammunition.Ammunition
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun AmmunitionDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    appViewModel.set(
        appBarTitle = stringResource(R.string.ammunition_details_title),
        navBarActions = listOf()
    )
    DetailsScreen<AmmunitionDetailsViewModel, Ammunition>(
        id,
        koinViewModel()
    ) { item, padding ->
        item.run {
            TextWithLabel(R.string.name, name)
            Spacer(Modifier.height(padding))
            TextWithLabel(R.string.sell_quantity, sellQuantity.toString())
            Spacer(Modifier.height(padding))
            TextWithLabel(R.string.cost, cost.toString())
            Spacer(Modifier.height(padding))
            TextWithLabel(R.string.weight, weight)
        }
    }
}