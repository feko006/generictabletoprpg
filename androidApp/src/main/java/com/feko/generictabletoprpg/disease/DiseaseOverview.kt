package com.feko.generictabletoprpg.disease

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object DiseaseOverview : OverviewScreen<DiseaseOverviewViewModel, Disease>() {
    override val screenTitle: String
        get() = "Diseases"
    override val route: String
        get() = "diseases"
    override val isRootDestination: Boolean
        get() = true
    override val detailsNavRouteProvider: Navigation.DetailsNavRouteProvider
        get() = DiseaseDetails

    @Composable
    override fun getViewModel(): DiseaseOverviewViewModel = koinViewModel()
}

