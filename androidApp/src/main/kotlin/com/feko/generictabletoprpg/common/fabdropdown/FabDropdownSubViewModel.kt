package com.feko.generictabletoprpg.common.fabdropdown

import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.fabdropdown.IFabDropdownSubViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FabDropdownSubViewModel(
    private val coroutineScope: CoroutineScope
) : IFabDropdownSubViewModel {
    override val isMenuExpanded: Flow<Boolean>
        get() = _isMenuExpanded
    val _isMenuExpanded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    suspend fun toggleFabDropdown() {
        _isMenuExpanded.emit(!_isMenuExpanded.value)
    }

    suspend fun collapse() {
        _isMenuExpanded.emit(false)
    }

    override fun toggleFabDropdownRequested() {
        coroutineScope.launch { toggleFabDropdown() }
    }

    override fun dismiss() {
        coroutineScope.launch { collapse() }
    }
}