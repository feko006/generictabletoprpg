package com.feko.generictabletoprpg.weapon

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.feko.generictabletoprpg.common.Common.TextWithLabel
import com.feko.generictabletoprpg.common.DetailsScreen
import com.feko.generictabletoprpg.common.DetailsViewModel
import org.koin.androidx.compose.koinViewModel


object WeaponDetails
    : DetailsScreen<WeaponDetailsViewModel, Weapon>(
    "Weapon Details",
    "weapon"
) {
    @Composable
    override fun getViewModel(): WeaponDetailsViewModel = koinViewModel()

    @Composable
    override fun ScreenContent(
        readiedItem: DetailsViewModel.DetailsScreenState.ItemReady<Weapon>,
        padding: Dp
    ) {
        readiedItem.item.run {
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