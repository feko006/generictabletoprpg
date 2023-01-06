package com.feko.generictabletoprpg.ammunition

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object AmmunitionOverview : OverviewScreen<AmmunitionOverviewViewModel, Ammunition>() {
    override val screenTitle: String
        get() = "Ammunitions"
    override val route: String
        get() = "ammunitions"
    override val isRootDestination: Boolean
        get() = true
    override val detailsNavRouteProvider: Navigation.DetailsNavRouteProvider
        get() = AmmunitionDetails

    @Composable
    override fun getViewModel(): AmmunitionOverviewViewModel = koinViewModel()
}

