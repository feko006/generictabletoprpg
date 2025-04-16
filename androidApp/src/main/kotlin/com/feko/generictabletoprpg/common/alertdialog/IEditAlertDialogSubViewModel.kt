package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.flow.StateFlow

interface IEditAlertDialogSubViewModel<T> : IAlertDialogSubViewModel {

    val editedItem: StateFlow<T>

    fun updateEditedItem(updatedItem: T)
}