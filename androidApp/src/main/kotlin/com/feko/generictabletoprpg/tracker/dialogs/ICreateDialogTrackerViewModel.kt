package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.tracker.TrackedThing

interface ICreateDialogTrackerViewModel {
    fun showCreateDialog(type: TrackedThing.Type)
}