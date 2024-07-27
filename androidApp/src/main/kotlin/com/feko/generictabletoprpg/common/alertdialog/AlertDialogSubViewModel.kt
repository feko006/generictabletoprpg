package com.feko.generictabletoprpg.common.alertdialog

import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AlertDialogSubViewModel(
    private val coroutineScope: CoroutineScope,
    private val onDismissed: () -> Unit = {}
) : IAlertDialogSubViewModel {
    @StringRes
    var _titleResource: Int = 0
    override val titleResource
        get() = _titleResource

    override val isVisible: Flow<Boolean>
        get() = _isVisible
    val _isVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun hide() {
        _isVisible.emit(false)
    }

    suspend fun show() {
        _isVisible.emit(true)
    }

    override fun dismiss() {
        coroutineScope.launch {
            hide()
            onDismissed()
        }
    }
}