package com.feko.generictabletoprpg.features.spell.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.features.spell.Spell
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>
@Composable
fun SimpleSpellDetailsScreen(
    spell: Spell,
    appViewModel: AppViewModel
) {
    SpellDetailsAppBarSetup(appViewModel)
    DetailsScreen(spell) { item, padding ->
        SpellDetailsContent(item, padding)
    }
}
