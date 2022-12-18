package com.feko.generictabletoprpg.action

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object ActionOverview : OverviewScreen<ActionOverviewViewModel, Action>() {
    override val screenTitle: String
        get() = "Actions"
    override val route: String
        get() = "actions"
    override val isRootDestination: Boolean
        get() = true
    override val detailsNavRouteProvider: Navigation.DetailsNavRouteProvider
        get() = ActionDetails

    @Composable
    override fun getViewModel(): ActionOverviewViewModel = koinViewModel()
}

