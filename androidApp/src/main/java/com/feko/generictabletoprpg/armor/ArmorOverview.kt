package com.feko.generictabletoprpg.armor

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object ArmorOverview : OverviewScreen<ArmorOverviewViewModel, Armor>() {
    override val route: String
        get() = "armorOverview"
    override val isRootDestination: Boolean
        get() = true
    override val screenTitle: String
        get() = "Armors"
    override val detailsNavRouteProvider: Navigation.DetailsNavRouteProvider
        get() = ArmorDetails

    @Composable
    override fun getViewModel(): ArmorOverviewViewModel = koinViewModel()
}