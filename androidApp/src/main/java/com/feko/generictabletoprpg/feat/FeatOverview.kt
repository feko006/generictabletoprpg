package com.feko.generictabletoprpg.feat

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object FeatOverview
    : OverviewScreen<FeatOverviewViewModel, Feat>(
    FeatDetails,
    "Feats",
    "feats"
) {
    @Composable
    override fun getViewModel(): FeatOverviewViewModel = koinViewModel()
}