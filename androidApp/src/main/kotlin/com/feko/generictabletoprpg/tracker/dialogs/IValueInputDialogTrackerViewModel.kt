package com.feko.generictabletoprpg.tracker.dialogs

interface IValueInputDialogTrackerViewModel
    : IBaseDialogTrackerViewModel,
    IValueTextFieldTrackerViewModel {
    fun updateValueInputField(delta: String)
}