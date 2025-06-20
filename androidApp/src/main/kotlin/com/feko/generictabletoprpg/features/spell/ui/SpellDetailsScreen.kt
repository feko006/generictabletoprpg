package com.feko.generictabletoprpg.features.spell.ui

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.features.spell.Spell
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.androidx.compose.koinViewModel

@Destination<RootGraph>
@Composable
fun SpellDetailsScreen(
    id: Long,
    appViewModel: AppViewModel
) {
    SpellDetailsAppBarSetup(appViewModel)
    DetailsScreen<SpellDetailsViewModel, Spell>(
        id,
        koinViewModel()
    ) { item, padding ->
        SpellDetailsContent(item, padding)
    }
}

