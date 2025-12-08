package com.feko.generictabletoprpg.shared.features.spell.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.features.spell.Spell
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SpellDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<SpellDetailsViewModel, Spell>(
        id,
        koinViewModel(),
        onNavigationIconClick
    ) { spell ->
        SpellDetailsContent(spell)
    }
}