package com.feko.generictabletoprpg.tracker.actions

import com.feko.generictabletoprpg.tracker.Stats

interface IStatsActionsTrackerViewModel : IBasicActionsTrackerViewModel {
    fun showStatsDialog(stats: Stats)
}