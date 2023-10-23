package com.feko.generictabletoprpg.common.fabdropdown

import kotlinx.coroutines.flow.MutableStateFlow

class FabDropdownViewModelExtension : IMutableFabDropdownViewModelExtension {
    override val _isFabDropdownMenuExpanded: MutableStateFlow<Boolean> = MutableStateFlow(false)
}