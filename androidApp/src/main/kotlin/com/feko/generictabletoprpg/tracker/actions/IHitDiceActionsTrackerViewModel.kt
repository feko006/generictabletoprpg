package com.feko.generictabletoprpg.tracker.actions

import com.feko.generictabletoprpg.tracker.TrackedThing

interface IHitDiceActionsTrackerViewModel : IBasicActionsTrackerViewModel {
    fun useHitDie(item: TrackedThing)
    fun restoreHitDie(item: TrackedThing)
}