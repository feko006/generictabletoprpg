package com.feko.generictabletoprpg.common.fabdropdown

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface IMutableFabDropdownViewModelExtension : IFabDropdownViewModelExtension {
    override val isFabDropdownMenuExpanded: Flow<Boolean>
        get() = _isFabDropdownMenuExpanded
    val _isFabDropdownMenuExpanded: MutableStateFlow<Boolean>

    override suspend fun toggleFabDropdown() {
        _isFabDropdownMenuExpanded.emit(!_isFabDropdownMenuExpanded.value)
    }

    override suspend fun dismissFabDropdown() {
        _isFabDropdownMenuExpanded.emit(false)
    }
}