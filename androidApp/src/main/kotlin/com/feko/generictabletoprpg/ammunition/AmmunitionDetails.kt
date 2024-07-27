package com.feko.generictabletoprpg.ammunition

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.composable.DetailsScreen
import com.feko.generictabletoprpg.common.composable.TextWithLabel
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination
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