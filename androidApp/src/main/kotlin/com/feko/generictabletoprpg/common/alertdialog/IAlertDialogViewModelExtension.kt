package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.flow.Flow

interface IAlertDialogViewModelExtension {
    val isDialogVisible: Flow<Boolean>
    var dialogTitleResource: Int
    suspend fun hideAlertDialog()
}