package com.feko.generictabletoprpg.common.toast

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ToastSubViewModel(
    private val coroutineScope: CoroutineScope
) : IToastSubViewModel {

    private val _shouldShowMessage = MutableSharedFlow<Boolean>()
    override val shouldShowMessage: SharedFlow<Boolean>
        get() = _shouldShowMessage

    @StringRes
    private var messageId: Int = 0
    private var messageArgs: List<String> = emptyList()

    @Composable
    override fun getMessage(): String {
        return stringResource(messageId, *messageArgs.toTypedArray())
    }

    fun showMessage(@StringRes messageId: Int, vararg args: String) {
        this.messageId = messageId
        messageArgs = args.toList()
        coroutineScope.launch {
            _shouldShowMessage.emit(true)
        }
    }

    override fun messageConsumed() {
        coroutineScope.launch {
            _shouldShowMessage.emit(false)
        }
    }

}