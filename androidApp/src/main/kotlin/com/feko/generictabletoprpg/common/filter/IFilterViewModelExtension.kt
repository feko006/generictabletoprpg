package com.feko.generictabletoprpg.common.filter

import com.feko.generictabletoprpg.filters.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IFilterViewModelExtension {
    val filter: StateFlow<Filter?>
    val filterButtonVisible: Flow<Boolean>
    fun filterRequested() {
        throw NotImplementedError()
    }

    suspend fun filterUpdated(newFilter: Filter)
}