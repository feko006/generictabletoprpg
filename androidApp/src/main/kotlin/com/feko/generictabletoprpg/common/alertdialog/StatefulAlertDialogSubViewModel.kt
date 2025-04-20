package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatefulAlertDialogSubViewModel<T>(
    defaultValue: T,
    private val coroutineScope: CoroutineScope,
    onDismissed: () -> Unit = {}
) : IMutableAlertDialogSubViewModel by AlertDialogSubViewModel(coroutineScope, onDismissed),
    IStatefulAlertDialogSubViewModel<T> {

    val _state = MutableStateFlow(defaultValue)
    override val state: StateFlow<T>
        get() = _state

    override fun updateState(newState: T) {
        coroutineScope.launch {
            _state.emit(newState)
        }
    }

    suspend fun show(newState: T) {
        _state.emit(newState)
        show()
    }
}