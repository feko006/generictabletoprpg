package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.tracker.TrackedThing
import kotlinx.coroutines.flow.MutableStateFlow

interface IEditDialogTrackerViewModel
    : IBaseDialogTrackerViewModel,
    INameTextFieldTrackerViewModel,
    ISpellSlotLevelTextFieldTrackerViewModel,
    IValueTextFieldTrackerViewModel {
    val editedTrackedThingType: MutableStateFlow<TrackedThing.Type>
    fun setValue(value: String)
}