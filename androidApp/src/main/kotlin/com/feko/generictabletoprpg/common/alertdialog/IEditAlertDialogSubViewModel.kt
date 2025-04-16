package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.flow.Flow

interface IEditAlertDialogSubViewModel<T> : IAlertDialogSubViewModel {

    val editedItem: Flow<T>

    fun updateEditedItem(updatedItem: T)
}