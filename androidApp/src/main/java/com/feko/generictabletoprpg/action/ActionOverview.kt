package com.feko.generictabletoprpg.action

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object ActionOverview
    : OverviewScreen<ActionOverviewViewModel, Action>(
    ActionDetails,
    "Actions",
    "actions"
) {
    @Composable
    override fun getViewModel(): ActionOverviewViewModel = koinViewModel()
}

