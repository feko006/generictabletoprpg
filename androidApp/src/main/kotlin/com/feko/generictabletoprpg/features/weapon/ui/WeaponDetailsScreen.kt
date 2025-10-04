package com.feko.generictabletoprpg.features.weapon.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import com.feko.generictabletoprpg.weapon_details_title
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeaponDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<WeaponDetailsViewModel, Weapon>(
        id,
        koinViewModel(),
        Res.string.weapon_details_title.asText(),
        onNavigationIconClick
    ) {
        TextWithLabel(R.string.name, it.name)
        TextWithLabel(R.string.type, it.type)
        if (it.subType.isNotBlank()) {
            TextWithLabel(R.string.subtype, it.subType)
        }
        TextWithLabel(R.string.damage, it.damage.toString())
        TextWithLabel(R.string.range, it.readableRange)
        if (it.hasAnyProperties) {
            TextWithLabel(R.string.properties, it.properties)
        }
    }
}