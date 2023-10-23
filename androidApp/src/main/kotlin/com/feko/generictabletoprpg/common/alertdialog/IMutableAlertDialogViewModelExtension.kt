package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface IMutableAlertDialogViewModelExtension : IAlertDialogViewModelExtension {
    override val isDialogVisible: Flow<Boolean>
        get() = _isDialogVisible
    val _isDialogVisible: MutableStateFlow<Boolean>

    override suspend fun hideAlertDialog() {
        _isDialogVisible.emit(false)
    }

    suspend fun showAlertDialog() {
        _isDialogVisible.emit(true)
    }
}