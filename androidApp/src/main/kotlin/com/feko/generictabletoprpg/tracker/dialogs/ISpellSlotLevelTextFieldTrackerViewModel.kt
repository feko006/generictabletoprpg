package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.common.composable.InputFieldData
import kotlinx.coroutines.flow.MutableStateFlow

interface ISpellSlotLevelTextFieldTrackerViewModel {
    val editedTrackedThingSpellSlotLevel: MutableStateFlow<InputFieldData>
    fun setLevel(level: String)
}