package com.feko.generictabletoprpg.common.fabdropdown

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object EmptyFabDropdownSubViewModel : IFabDropdownSubViewModel {
    override val isMenuExpanded: Flow<Boolean>
        get() = emptyFlow()

    override fun toggleFabDropdownRequested() = Unit

    override fun dismiss() = Unit
}