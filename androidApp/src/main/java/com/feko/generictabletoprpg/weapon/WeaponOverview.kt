package com.feko.generictabletoprpg.weapon

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object WeaponOverview
    : OverviewScreen<WeaponOverviewViewModel, Weapon>(
    WeaponDetails,
    "Weapons",
    "weapons"
) {
    @Composable
    override fun getViewModel(): WeaponOverviewViewModel = koinViewModel()
}