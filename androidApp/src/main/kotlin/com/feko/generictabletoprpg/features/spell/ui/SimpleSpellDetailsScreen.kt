package com.feko.generictabletoprpg.features.spell.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.features.spell.Spell

@Composable
fun SimpleSpellDetailsScreen(
    spell: Spell,
    onNavigationIconClick: () -> Unit
) {
    Scaffold(
        topBar = { GttrpgTopAppBar(R.string.spell_details_title.asText(), onNavigationIconClick) }
    ) { it: PaddingValues ->
        DetailsScreen(spell, Modifier.padding(it)) {
            SpellDetailsContent(spell)
        }
    }
}
