package com.feko.generictabletoprpg.common.toast

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object EmptyToastSubViewModel : IToastSubViewModel {
    override val shouldShowMessage: SharedFlow<Boolean>
        get() = MutableSharedFlow()

    @Composable
    override fun getMessage(): String = ""

    override fun messageConsumed() = Unit
}