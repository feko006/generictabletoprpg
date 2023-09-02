package com.feko.generictabletoprpg.disease

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object DiseaseOverview
    : OverviewScreen<DiseaseOverviewViewModel, Disease>(
    DiseaseDetails,
    "Diseases",
    "diseases"
) {
    @Composable
    override fun getViewModel(): DiseaseOverviewViewModel = koinViewModel()
}

