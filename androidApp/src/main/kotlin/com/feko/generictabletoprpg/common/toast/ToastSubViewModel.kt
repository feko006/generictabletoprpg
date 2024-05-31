package com.feko.generictabletoprpg.common.toast

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ToastSubViewModel : IToastSubViewModel {
    override val message: SharedFlow<Int>
        get() = _message
    val _message: MutableSharedFlow<Int> = MutableSharedFlow(0)

    override fun messageConsumed() {
        CoroutineScope(Dispatchers.Main).launch {
            _message.emit(0)
        }
    }
}