package com.feko.generictabletoprpg.common.alertdialog

import com.feko.generictabletoprpg.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object EmptyAlertDialogSubViewModel : IAlertDialogSubViewModel {
    override val titleResource: Int
        get() = R.string.text
    override val isVisible: Flow<Boolean>
        get() = emptyFlow()

    override fun dismiss() = Unit
}