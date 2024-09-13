package com.feko.generictabletoprpg.tracker.actions

import com.feko.generictabletoprpg.tracker.TrackedThing

interface IResetActionTrackerViewModel {
    fun resetValueToDefault(item: TrackedThing)
}