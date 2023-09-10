package com.feko.generictabletoprpg.weapon

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.DetailsScreen
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.TextWithLabel
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun WeaponDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    appViewModel.set(appBarTitle = "Weapon Details", navBarActions = listOf())
    DetailsScreen<WeaponDetailsViewModel, Weapon>(
        id,
        koinViewModel()
    ) { item, padding ->
        item.run {
            TextWithLabel("Name", name)
            Spacer(Modifier.height(padding))
            TextWithLabel("Type", type)
            if (subType.isNotBlank()) {
                Spacer(Modifier.height(padding))
                TextWithLabel("Type", subType)
            }
            Spacer(Modifier.height(padding))
            TextWithLabel("Damage", damage.toString())
            Spacer(Modifier.height(padding))
            TextWithLabel("Range", readableRange)
            if (hasAnyProperties) {
                Spacer(Modifier.height(padding))
                TextWithLabel("Properties", properties)
            }
        }
    }
}