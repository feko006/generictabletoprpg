package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.common.composable.InputFieldData
import kotlinx.coroutines.flow.MutableStateFlow

interface IValueTextFieldTrackerViewModel : IConfirmDialogTrackerViewModel {
    val editedTrackedThingValue: MutableStateFlow<InputFieldData>
}