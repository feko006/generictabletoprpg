package com.feko.generictabletoprpg.common.alertdialog

import androidx.annotation.StringRes
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AlertDialogSubViewModel(
    private val coroutineScope: CoroutineScope
) : IAlertDialogSubViewModel {
    @StringRes
    override var titleResource: Int = 0

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
        coroutineScope.launch { hide() }
    }
}