package com.feko.generictabletoprpg.weapon

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object WeaponOverview : OverviewScreen<WeaponOverviewViewModel, Weapon>() {
    override val route: String
        get() = "weaponOverview"
    override val isRootDestination: Boolean
        get() = true
    override val screenTitle: String
        get() = "Weapons"
    override val detailsNavRouteProvider: Navigation.DetailsNavRouteProvider
        get() = WeaponDetails

    @Composable
    override fun getViewModel(): WeaponOverviewViewModel = koinViewModel()
}