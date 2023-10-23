package com.feko.generictabletoprpg.common.alertdialog

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow

class AlertDialogViewModelExtension : IMutableAlertDialogViewModelExtension {
    override val _isDialogVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    @StringRes
    override var dialogTitleResource: Int = 0
}