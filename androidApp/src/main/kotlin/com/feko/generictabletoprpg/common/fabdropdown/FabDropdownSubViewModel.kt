package com.feko.generictabletoprpg.common.fabdropdown

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FabDropdownSubViewModel(
    private val coroutineScope: CoroutineScope
) {
    val isMenuExpanded: Flow<Boolean>
        get() = _isFabDropdownMenuExpanded
    val _isFabDropdownMenuExpanded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun toggleFabDropdown() {
        _isFabDropdownMenuExpanded.emit(!_isFabDropdownMenuExpanded.value)
    }

    suspend fun collapse() {
        _isFabDropdownMenuExpanded.emit(false)
    }

    fun toggleFabDropdownRequested() {
        coroutineScope.launch { toggleFabDropdown() }
    }

    fun dismiss() {
        coroutineScope.launch { collapse() }
    }
}