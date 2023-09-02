package com.feko.generictabletoprpg.ammunition

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object AmmunitionOverview
    : OverviewScreen<AmmunitionOverviewViewModel, Ammunition>(
    AmmunitionDetails,
    "Ammunitions",
    "ammunitions"
) {
    @Composable
    override fun getViewModel(): AmmunitionOverviewViewModel = koinViewModel()
}

