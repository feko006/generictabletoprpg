package com.feko.generictabletoprpg.shared.features.magicitem.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.rarity
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.features.magicitem.MagicItem
import com.feko.generictabletoprpg.type
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MagicItemDetailsScreen(id: Long, onNavigationIconClick: () -> Unit) {
    DetailsScreen<MagicItemViewModel, MagicItem>(
        id,
        koinViewModel(),
        onNavigationIconClick
    ) {
        TextWithLabel(Res.string.type, it.type)
        TextWithLabel(Res.string.rarity, it.rarity)
        HorizontalDivider()
        Text(it.description)
    }
}