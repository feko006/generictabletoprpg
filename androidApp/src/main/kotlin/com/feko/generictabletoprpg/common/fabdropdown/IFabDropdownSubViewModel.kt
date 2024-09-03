package com.feko.generictabletoprpg.common.fabdropdown

import kotlinx.coroutines.flow.Flow

interface IFabDropdownSubViewModel {
    val isMenuExpanded: Flow<Boolean>
    fun toggleFabDropdownRequested()
    fun dismiss()
}
