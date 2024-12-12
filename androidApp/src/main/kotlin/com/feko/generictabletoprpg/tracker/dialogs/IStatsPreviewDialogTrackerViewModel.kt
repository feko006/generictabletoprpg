package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.tracker.StatsContainer

interface IStatsPreviewDialogTrackerViewModel : IBaseDialogTrackerViewModel {
    val statsBeingPreviewed: StatsContainer?
}
