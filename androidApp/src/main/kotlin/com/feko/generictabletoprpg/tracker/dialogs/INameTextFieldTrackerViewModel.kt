package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.common.composable.InputFieldData
import kotlinx.coroutines.flow.MutableStateFlow

interface INameTextFieldTrackerViewModel {
    val editedTrackedThingName: MutableStateFlow<InputFieldData>
    fun setName(name: String)
}