package com.feko.generictabletoprpg.condition

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.common.OverviewScreen
import org.koin.androidx.compose.koinViewModel

object ConditionOverview
    : OverviewScreen<ConditionOverviewViewModel, Condition>(
    ConditionDetails,
    "Conditions",
    "conditions"
) {
    @Composable
    override fun getViewModel(): ConditionOverviewViewModel = koinViewModel()
}

