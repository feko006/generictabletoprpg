package com.feko.generictabletoprpg.common.fabdropdown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

fun <T> T.onDismissFabDropdownRequested()
        where T : ViewModel,
              T : IFabDropdownViewModelExtension {
    viewModelScope.launch {
        dismissFabDropdown()
    }
}

fun <T> T.toggleFabDropdownRequested()
        where T : ViewModel,
              T : IFabDropdownViewModelExtension {
    viewModelScope.launch {
        toggleFabDropdown()
    }
}
