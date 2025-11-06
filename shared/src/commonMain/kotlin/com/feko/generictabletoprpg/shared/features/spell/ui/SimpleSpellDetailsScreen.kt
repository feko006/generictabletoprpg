package com.feko.generictabletoprpg.shared.features.spell.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.spell_details_title

@Composable
fun SimpleSpellDetailsScreen(
    spell: Spell,
    onNavigationIconClick: () -> Unit
) {
    Scaffold(
        topBar = { GttrpgTopAppBar(Res.string.spell_details_title.asText(), onNavigationIconClick) }
    ) { it: PaddingValues ->
        DetailsScreen(spell, Modifier.padding(it)) {
            SpellDetailsContent(spell)
        }
    }
}