package com.feko.generictabletoprpg.common.toast

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.SharedFlow

interface IToastSubViewModel {
    val shouldShowMessage: SharedFlow<Boolean>

    @Composable
    fun getMessage(): String
    fun messageConsumed()
}