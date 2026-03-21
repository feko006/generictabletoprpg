package com.feko.generictabletoprpg.shared.features.magicitem.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.attunement
import com.feko.generictabletoprpg.rarity
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.features.magicitem.MagicItem
import com.feko.generictabletoprpg.type
import com.feko.generictabletoprpg.yes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MagicItemDetailsScreen(id: Long, onNavigationIconClick: () -> Unit) {
    DetailsScreen<MagicItemViewModel, MagicItem>(
        id,
        koinViewModel(),
        onNavigationIconClick
    ) {
        MagicItemDetailsContent(it)
    }
}

@Composable
fun MagicItemDetailsContent(item: MagicItem) {
    TextWithLabel(Res.string.type, item.type)
    TextWithLabel(Res.string.rarity, item.rarity)
    if (item.attunement) {
        TextWithLabel(Res.string.attunement, stringResource(Res.string.yes))
    }
    HorizontalDivider()
    Text(item.description)
}