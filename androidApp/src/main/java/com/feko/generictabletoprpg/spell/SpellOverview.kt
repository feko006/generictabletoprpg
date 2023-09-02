package com.feko.generictabletoprpg.spell

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object SpellOverview
    : OverviewScreen<SpellOverviewViewModel, Spell>(
    SpellDetails,
    "Spells",
    "spells"
) {
    @Composable
    override fun getViewModel(): SpellOverviewViewModel = koinViewModel()
}