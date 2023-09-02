package com.feko.generictabletoprpg.armor

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object ArmorOverview
    : OverviewScreen<ArmorOverviewViewModel, Armor>(
    ArmorDetails,
    "Armors",
    "armors"
) {
    @Composable
    override fun getViewModel(): ArmorOverviewViewModel = koinViewModel()
}