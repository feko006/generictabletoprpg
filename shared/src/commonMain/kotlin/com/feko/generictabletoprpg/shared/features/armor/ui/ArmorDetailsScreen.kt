package com.feko.generictabletoprpg.shared.features.armor.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.base_ac
import com.feko.generictabletoprpg.maximum_dex_modifier
import com.feko.generictabletoprpg.minimum_str
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
        onNavigationIconClick
    ) {
        ArmorDetailsContent(it)
    }
}

@Composable
fun ArmorDetailsContent(armor: Armor) {
    TextWithLabel(Res.string.type, armor.type)
    armor.baseAc?.let { TextWithLabel(Res.string.base_ac, it.toString()) }
    armor.maxDexModifier?.let { TextWithLabel(Res.string.maximum_dex_modifier, it.toString()) }
    armor.stealthDisadvantage?.let {
        TextWithLabel(
            Res.string.stealth_disadvantage,
            it.toString()
        )
    }
    armor.weight?.let { weight -> TextWithLabel(Res.string.weight, armor.weightInLbs) }
    armor.minimumStrength?.let { TextWithLabel(Res.string.minimum_str, it.toString()) }
}