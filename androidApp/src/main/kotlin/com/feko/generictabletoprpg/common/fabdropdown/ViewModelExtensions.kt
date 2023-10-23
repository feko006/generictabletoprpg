package com.feko.generictabletoprpg.common.fabdropdown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

fun <T> T.onDismissFabDropdownMenuRequested()
        where T : ViewModel,
              T : IFabDropdownViewModelExtension {
    viewModelScope.launch {
        dismiss()
    }
}

fun <T> T.toggleFabDropdownMenu()
        where T : ViewModel,
              T : IFabDropdownViewModelExtension {
    viewModelScope.launch {
        toggle()
    }
}
