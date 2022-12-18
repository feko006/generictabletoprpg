package com.feko.generictabletoprpg.condition

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object ConditionOverview : OverviewScreen<ConditionOverviewViewModel, Condition>() {
    override val screenTitle: String
        get() = "Conditions"
    override val route: String
        get() = "conditions"
    override val isRootDestination: Boolean
        get() = true
    override val detailsNavRouteProvider: Navigation.DetailsNavRouteProvider
        get() = ConditionDetails

    @Composable
    override fun getViewModel(): ConditionOverviewViewModel = koinViewModel()
}

