package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import com.feko.generictabletoprpg.shared.features.filter.Filter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FilterViewModel(
    private val defaultFilter: Filter? = null
) : IFilterViewModel {
    override val activeFilter: StateFlow<Filter?>
        get() = _activeFilter
    private val _activeFilter = MutableStateFlow(defaultFilter)

    override fun filterUpdated(newFilter: Filter?) {
        _activeFilter.update { newFilter }
    }

    override fun resetFilter() {
        _activeFilter.update { defaultFilter }
    }
}
