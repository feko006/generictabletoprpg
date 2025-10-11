package com.feko.generictabletoprpg.features.spell.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.spell.ui.SpellDetailsViewModel
import com.feko.generictabletoprpg.spell_details_title
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SpellDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<SpellDetailsViewModel, Spell>(
        id,
        koinViewModel(),
        Res.string.spell_details_title.asText(),
        onNavigationIconClick
    ) { spell ->
        SpellDetailsContent(spell)
    }
}

