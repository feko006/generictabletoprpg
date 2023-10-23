package com.feko.generictabletoprpg.common.alertdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

fun <T> T.onAlertDialogDismissRequested()
        where T : ViewModel,
              T : IAlertDialogViewModelExtension {
    viewModelScope.launch {
        hideAlertDialog()
    }
}
