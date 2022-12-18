package com.feko.generictabletoprpg.feat

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel


object FeatOverview : OverviewScreen<FeatOverviewViewModel, Feat>() {
    override val screenTitle: String
        get() = "Feats"
    override val route: String
        get() = "featOverview"
    override val isRootDestination: Boolean
        get() = true

    @Composable
    override fun getViewModel(): FeatOverviewViewModel = koinViewModel()
    override val detailsNavRouteProvider: Navigation.DetailsNavRouteProvider
        get() = FeatDetails
}