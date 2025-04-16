package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.flow.MutableStateFlow

interface IMutableAlertDialogSubViewModel : IAlertDialogSubViewModel {
    var _titleResource: Int
    val _isVisible: MutableStateFlow<Boolean>
    suspend fun hide()
    suspend fun show()
}