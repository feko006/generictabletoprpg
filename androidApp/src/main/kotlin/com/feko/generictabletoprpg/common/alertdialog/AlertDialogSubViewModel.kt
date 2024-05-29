package com.feko.generictabletoprpg.common.alertdialog

import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AlertDialogSubViewModel(
    private val coroutineScope: CoroutineScope
) {
    @StringRes
    var titleResource: Int = 0

    val isVisible: Flow<Boolean>
        get() = _isDialogVisible
    val _isDialogVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun hide() {
        _isDialogVisible.emit(false)
    }

    suspend fun show() {
        _isDialogVisible.emit(true)
    }

    fun dismiss() {
        coroutineScope.launch { hide() }
    }
}