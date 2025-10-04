package com.feko.generictabletoprpg.features.armor.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.armor_details_title
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.features.armor.Armor
import org.koin.androidx.compose.koinViewModel

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

        TextWithLabel(R.string.name, it.name)
        TextWithLabel(R.string.type, it.type)
        it.baseAc?.let { TextWithLabel(R.string.base_ac, it.toString()) }
        it.maxDexModifier?.let { TextWithLabel(R.string.maximum_dex_modifier, it.toString()) }
        it.stealthDisadvantage?.let { TextWithLabel(R.string.stealth_disadvantage, it.toString()) }
        it.weight?.let { weight -> TextWithLabel(R.string.weight, it.weightInLbs) }
        it.minimumStrength?.let { TextWithLabel(R.string.minimum_str, it.toString()) }
    }
}