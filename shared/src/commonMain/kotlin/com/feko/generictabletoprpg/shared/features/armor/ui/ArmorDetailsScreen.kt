package com.feko.generictabletoprpg.shared.features.armor.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.armor_details_title
import com.feko.generictabletoprpg.base_ac
import com.feko.generictabletoprpg.maximum_dex_modifier
import com.feko.generictabletoprpg.minimum_str
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.features.armor.Armor
import com.feko.generictabletoprpg.stealth_disadvantage
import com.feko.generictabletoprpg.type
import com.feko.generictabletoprpg.weight
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ArmorDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<ArmorDetailsViewModel, Armor>(
        id,
        koinViewModel(),
        Res.string.armor_details_title.asText(),
        onNavigationIconClick
    ) {

        TextWithLabel(Res.string.name, it.name)
        TextWithLabel(Res.string.type, it.type)
        it.baseAc?.let { TextWithLabel(Res.string.base_ac, it.toString()) }
        it.maxDexModifier?.let { TextWithLabel(Res.string.maximum_dex_modifier, it.toString()) }
        it.stealthDisadvantage?.let {
            TextWithLabel(
                Res.string.stealth_disadvantage,
                it.toString()
            )
        }
        it.weight?.let { weight -> TextWithLabel(Res.string.weight, it.weightInLbs) }
        it.minimumStrength?.let { TextWithLabel(Res.string.minimum_str, it.toString()) }
    }
}