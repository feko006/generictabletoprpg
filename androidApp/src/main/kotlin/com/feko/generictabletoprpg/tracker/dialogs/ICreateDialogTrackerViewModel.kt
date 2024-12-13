package com.feko.generictabletoprpg.tracker.dialogs

import android.content.Context
import com.feko.generictabletoprpg.tracker.TrackedThing

interface ICreateDialogTrackerViewModel {
    fun showCreateDialog(type: TrackedThing.Type, context: Context)
}