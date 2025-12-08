package com.feko.generictabletoprpg.shared.features.spell.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.features.spell.Spell

@Composable
fun SimpleSpellDetailsScreen(
    spell: Spell,
    onNavigationIconClick: () -> Unit
) {
    Scaffold(
        topBar = { GttrpgTopAppBar(spell.name.asText(), onNavigationIconClick) }
    ) { it: PaddingValues ->
        DetailsScreen(spell, Modifier.padding(it)) {
            SpellDetailsContent(spell)
        }
    }
}