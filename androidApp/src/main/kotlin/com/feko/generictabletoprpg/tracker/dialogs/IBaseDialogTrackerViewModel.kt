package com.feko.generictabletoprpg.tracker.dialogs

import kotlinx.coroutines.flow.MutableStateFlow

interface IBaseDialogTrackerViewModel : IConfirmDialogTrackerViewModel {
    val confirmButtonEnabled: MutableStateFlow<Boolean>
}