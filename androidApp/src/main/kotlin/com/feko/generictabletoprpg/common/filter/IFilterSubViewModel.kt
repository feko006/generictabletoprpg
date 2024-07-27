package com.feko.generictabletoprpg.common.filter

import com.feko.generictabletoprpg.filters.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IFilterSubViewModel {
    val activeFilter: StateFlow<Filter?>
    val isButtonVisible: Flow<Boolean>
    val offButtonVisible: Flow<Boolean>
    fun filterUpdated(newFilter: Filter?)
    fun resetFilter()
}