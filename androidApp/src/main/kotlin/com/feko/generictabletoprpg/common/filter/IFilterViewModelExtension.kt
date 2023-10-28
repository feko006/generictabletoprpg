package com.feko.generictabletoprpg.common.filter

import com.feko.generictabletoprpg.filters.Filter
import kotlinx.coroutines.flow.Flow

interface IFilterViewModelExtension {
    val filter: Flow<Filter?>
    val filterButtonVisible: Flow<Boolean>
    fun filterRequested() {
        throw NotImplementedError()
    }

    suspend fun filterUpdated(newFilter: Filter)
}