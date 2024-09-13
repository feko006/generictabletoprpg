package com.feko.generictabletoprpg.tracker.actions

import com.feko.generictabletoprpg.tracker.TrackedThing

interface INumberActionsTrackerViewModel : IBasicActionsTrackerViewModel {
    fun addToNumberRequested(item: TrackedThing)
    fun subtractFromNumberRequested(item: TrackedThing)
}