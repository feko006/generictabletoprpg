package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EditAlertDialogSubViewModel<T>(
    defaultValue: T,
    private val coroutineScope: CoroutineScope,
    onDismissed: () -> Unit = {}
) : IMutableAlertDialogSubViewModel by AlertDialogSubViewModel(coroutineScope, onDismissed),
    IEditAlertDialogSubViewModel<T> {

    val _editedItem = MutableStateFlow(defaultValue)
    override val editedItem: Flow<T>
        get() = _editedItem

    override fun updateEditedItem(updatedItem: T) {
        coroutineScope.launch {
            _editedItem.emit(updatedItem)
        }
    }

    suspend fun show(itemToEdit: T) {
        _editedItem.emit(itemToEdit)
        show()
    }
}