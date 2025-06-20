package com.feko.generictabletoprpg.common.ui.viewmodel

import kotlinx.coroutines.flow.Flow

interface IFabDropdownSubViewModel {
    val isMenuExpanded: Flow<Boolean>
    fun toggleFabDropdownRequested()
    fun dismiss()
}
