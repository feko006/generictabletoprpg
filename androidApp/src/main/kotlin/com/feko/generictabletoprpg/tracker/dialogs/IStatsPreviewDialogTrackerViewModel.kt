package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.tracker.StatsContainer
import com.feko.generictabletoprpg.tracker.dialogs.IBaseDialogTrackerViewModel

interface IStatsPreviewDialogTrackerViewModel : IBaseDialogTrackerViewModel {
    val statsBeingPreviewed: StatsContainer?
}
