package com.feko.generictabletoprpg.spell

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object SpellOverview : OverviewScreen<SpellOverviewViewModel, Spell>() {
    override val route: String
        get() = "spellOverview"
    override val isRootDestination: Boolean
        get() = true
    override val screenTitle: String
        get() = "Spells"
    override val detailsNavRouteProvider: Navigation.DetailsNavRouteProvider
        get() = SpellDetails

    @Composable
    override fun getViewModel(): SpellOverviewViewModel = koinViewModel()
}