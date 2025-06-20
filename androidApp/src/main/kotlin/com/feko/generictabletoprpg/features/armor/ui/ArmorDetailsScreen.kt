package com.feko.generictabletoprpg.features.armor.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.features.armor.Armor
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun ArmorDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    appViewModel.set(
        appBarTitle = stringResource(R.string.armor_details_title),
        navBarActions = listOf()
    )
    DetailsScreen<ArmorDetailsViewModel, Armor>(
        id,
        koinViewModel()
    ) { item, padding ->

        item.run {
            TextWithLabel(R.string.name, name)
            Spacer(Modifier.height(padding))
            TextWithLabel(R.string.type, type)
            Spacer(Modifier.height(padding))
            baseAc?.let {
                TextWithLabel(R.string.base_ac, it.toString())
                Spacer(Modifier.height(padding))
            }
            maxDexModifier?.let {
                TextWithLabel(R.string.maximum_dex_modifier, it.toString())
                Spacer(Modifier.height(padding))
            }
            stealthDisadvantage?.let {
                TextWithLabel(R.string.stealth_disadvantage, it.toString())
                Spacer(Modifier.height(padding))
            }
            weight?.let {
                TextWithLabel(R.string.weight, weightInLbs)
                Spacer(Modifier.height(padding))
            }
            minimumStrength?.let {
                TextWithLabel(R.string.minimum_str, minimumStrength.toString())
                Spacer(Modifier.height(padding))
            }
        }
    }
}