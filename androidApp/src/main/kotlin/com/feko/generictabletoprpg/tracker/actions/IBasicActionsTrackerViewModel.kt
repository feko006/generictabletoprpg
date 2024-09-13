package com.feko.generictabletoprpg.tracker.actions

import com.feko.generictabletoprpg.tracker.TrackedThing

interface IBasicActionsTrackerViewModel {
    fun showEditDialog(item: TrackedThing)
    fun deleteItemRequested(item: TrackedThing)
}

