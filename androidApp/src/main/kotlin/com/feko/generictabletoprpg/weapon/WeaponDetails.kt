package com.feko.generictabletoprpg.weapon

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