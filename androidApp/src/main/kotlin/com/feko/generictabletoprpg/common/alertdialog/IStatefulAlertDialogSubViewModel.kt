package com.feko.generictabletoprpg.common.alertdialog

import kotlinx.coroutines.flow.StateFlow

interface IStatefulAlertDialogSubViewModel<T> : IAlertDialogSubViewModel {

    val state: StateFlow<T>

    fun updateState(newState: T)
}