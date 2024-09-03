package com.feko.generictabletoprpg.tracker.actions

import com.feko.generictabletoprpg.tracker.TrackedThing

interface IAbilityActionsTrackerViewModel
    : IBasicActionsTrackerViewModel,
    IResetActionTrackerViewModel {
    fun useAbility(item: TrackedThing)
}