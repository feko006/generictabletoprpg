package com.feko.generictabletoprpg.tracker.actions

import com.feko.generictabletoprpg.tracker.TrackedThing

interface IPercentageActionsTrackerViewModel : IBasicActionsTrackerViewModel {
    fun addToPercentageRequested(item: TrackedThing)
    fun subtractFromPercentageRequested(item: TrackedThing)
}