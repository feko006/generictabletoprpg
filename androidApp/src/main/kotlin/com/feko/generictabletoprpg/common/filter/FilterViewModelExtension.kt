package com.feko.generictabletoprpg.common.filter

import com.feko.generictabletoprpg.filters.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FilterViewModelExtension : IMutableFilterViewModelExtension {
    override val filter: Flow<Filter?>
        get() = _filter
    private val _filter = MutableStateFlow<Filter?>(null)
    override val filterButtonVisible: Flow<Boolean>
        get() = _filterButtonVisible
    private val _filterButtonVisible = MutableStateFlow(false)

    override suspend fun filterUpdated(newFilter: Filter) {
        _filter.emit(newFilter)
    }

    override suspend fun setFilterButtonVisible(isVisible: Boolean) {
        _filterButtonVisible.emit(isVisible)
    }

}
