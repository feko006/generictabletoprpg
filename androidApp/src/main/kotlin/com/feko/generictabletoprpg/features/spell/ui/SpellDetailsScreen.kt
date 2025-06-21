package com.feko.generictabletoprpg.features.spell.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.features.spell.Spell
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun SpellDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<SpellDetailsViewModel, Spell>(
        id,
        koinViewModel(),
        R.string.spell_details_title.asText(),
        onNavigationIconClick
    ) { spell ->
        SpellDetailsContent(spell)
    }
}

