package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

class ToastSubViewModel(
    private val coroutineScope: CoroutineScope
) : IToastSubViewModel {

    private val _shouldShowMessage = MutableSharedFlow<Boolean>()
    override val shouldShowMessage: SharedFlow<Boolean>
        get() = _shouldShowMessage

    private lateinit var messageId: StringResource
    private var messageArgs: List<String> = emptyList()

    @Composable
    override fun getMessage(): String {
        return stringResource(messageId, *messageArgs.toTypedArray())
    }

    fun showMessage(messageId: StringResource, vararg args: String) {
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