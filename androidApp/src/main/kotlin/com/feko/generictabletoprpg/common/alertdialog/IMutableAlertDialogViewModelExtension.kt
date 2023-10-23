package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface IMutableAlertDialogViewModelExtension : IAlertDialogViewModelExtension {
    override val isDialogVisible: Flow<Boolean>
        get() = _isDialogVisible
    val _isDialogVisible: MutableStateFlow<Boolean>

    suspend fun hide() {
        _isDialogVisible.emit(false)
    }

    suspend fun show() {
        _isDialogVisible.emit(true)
    }
}