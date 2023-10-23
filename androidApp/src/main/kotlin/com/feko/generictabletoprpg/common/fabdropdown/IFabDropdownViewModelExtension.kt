package com.feko.generictabletoprpg.common.fabdropdown

import kotlinx.coroutines.flow.Flow

interface IFabDropdownViewModelExtension {
    val isFabDropdownMenuExpanded: Flow<Boolean>
    suspend fun toggleFabDropdown()
    suspend fun dismissFabDropdown()
}