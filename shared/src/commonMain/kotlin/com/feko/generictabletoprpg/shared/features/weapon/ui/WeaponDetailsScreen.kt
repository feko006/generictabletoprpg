package com.feko.generictabletoprpg.shared.features.weapon.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.damage
import com.feko.generictabletoprpg.properties
import com.feko.generictabletoprpg.range
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import com.feko.generictabletoprpg.subtype
import com.feko.generictabletoprpg.type
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WeaponDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<WeaponDetailsViewModel, Weapon>(
        id,
        koinViewModel(),
        onNavigationIconClick
    ) {
        WeaponDetailsContent(it)
    }
}

@Composable
fun WeaponDetailsContent(weapon: Weapon) {
    TextWithLabel(Res.string.type, weapon.type)
    if (weapon.subType.isNotBlank()) {
        TextWithLabel(Res.string.subtype, weapon.subType)
    }
    TextWithLabel(Res.string.damage, weapon.damage.toString())
    TextWithLabel(Res.string.range, weapon.readableRange)
    if (weapon.hasAnyProperties) {
        TextWithLabel(Res.string.properties, weapon.properties)
    }
}