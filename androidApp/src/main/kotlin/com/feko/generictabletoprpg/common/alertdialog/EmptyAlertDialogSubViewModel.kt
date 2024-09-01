package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object EmptyAlertDialogSubViewModel : IAlertDialogSubViewModel {
    override val titleResource: Int
        get() = 0
    override val isVisible: Flow<Boolean>
        get() = emptyFlow()

    override fun dismiss() = Unit
}