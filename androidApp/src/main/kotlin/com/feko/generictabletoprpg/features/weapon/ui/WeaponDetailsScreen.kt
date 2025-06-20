package com.feko.generictabletoprpg.features.weapon.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.features.weapon.Weapon
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun WeaponDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    appViewModel.set(
        appBarTitle = stringResource(R.string.weapon_details_title),
        navBarActions = listOf()
    )
    DetailsScreen<WeaponDetailsViewModel, Weapon>(
        id,
        koinViewModel()
    ) { item, padding ->
        item.run {
            TextWithLabel(R.string.name, name)
            Spacer(Modifier.height(padding))
            TextWithLabel(R.string.type, type)
            if (subType.isNotBlank()) {
                Spacer(Modifier.height(padding))
                TextWithLabel(R.string.subtype, subType)
            }
            Spacer(Modifier.height(padding))
            TextWithLabel(R.string.damage, damage.toString())
            Spacer(Modifier.height(padding))
            TextWithLabel(R.string.range, readableRange)
            if (hasAnyProperties) {
                Spacer(Modifier.height(padding))
                TextWithLabel(R.string.properties, properties)
            }
        }
    }
}