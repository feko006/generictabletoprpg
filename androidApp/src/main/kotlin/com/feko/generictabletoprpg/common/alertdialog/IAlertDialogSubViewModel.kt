package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.flow.Flow

interface IAlertDialogSubViewModel {
    var titleResource: Int
    val isVisible: Flow<Boolean>
    fun dismiss()

}
