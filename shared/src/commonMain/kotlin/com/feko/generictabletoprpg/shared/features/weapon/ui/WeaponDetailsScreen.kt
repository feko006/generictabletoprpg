package com.feko.generictabletoprpg.shared.features.weapon.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.damage
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.properties
import com.feko.generictabletoprpg.range
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import com.feko.generictabletoprpg.subtype
import com.feko.generictabletoprpg.type
import com.feko.generictabletoprpg.weapon_details_title
import org.koin.compose.viewmodel.koinViewModel

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
        TextWithLabel(Res.string.name, it.name)
        TextWithLabel(Res.string.type, it.type)
        if (it.subType.isNotBlank()) {
            TextWithLabel(Res.string.subtype, it.subType)
        }
        TextWithLabel(Res.string.damage, it.damage.toString())
        TextWithLabel(Res.string.range, it.readableRange)
        if (it.hasAnyProperties) {
            TextWithLabel(Res.string.properties, it.properties)
        }
    }
}