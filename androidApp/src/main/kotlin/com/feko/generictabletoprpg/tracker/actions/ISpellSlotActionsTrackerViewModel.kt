package com.feko.generictabletoprpg.tracker.actions

import com.feko.generictabletoprpg.tracker.TrackedThing

interface ISpellSlotActionsTrackerViewModel
    : IBasicActionsTrackerViewModel,
    IResetActionTrackerViewModel {
    fun useSpell(item: TrackedThing)
}