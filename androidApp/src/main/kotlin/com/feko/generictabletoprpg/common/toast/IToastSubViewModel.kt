package com.feko.generictabletoprpg.common.toast

import kotlinx.coroutines.flow.SharedFlow

interface IToastSubViewModel {
    val message: SharedFlow<Int>
    fun messageConsumed()
}