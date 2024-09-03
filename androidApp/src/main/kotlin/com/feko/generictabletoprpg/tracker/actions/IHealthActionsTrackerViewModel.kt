package com.feko.generictabletoprpg.tracker.actions

import com.feko.generictabletoprpg.tracker.TrackedThing

interface IHealthActionsTrackerViewModel
    : IBasicActionsTrackerViewModel,
    IResetActionTrackerViewModel {
    fun takeDamageRequested(item: TrackedThing)
    fun healRequested(item: TrackedThing)
    fun addTemporaryHp(item: TrackedThing)
}