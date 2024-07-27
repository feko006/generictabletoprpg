package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.flow.Flow

interface IAlertDialogSubViewModel {
    val titleResource: Int
    val isVisible: Flow<Boolean>
    fun dismiss()

}
