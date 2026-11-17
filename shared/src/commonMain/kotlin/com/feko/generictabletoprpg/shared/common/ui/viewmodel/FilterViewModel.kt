package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import com.feko.generictabletoprpg.shared.features.filter.Filter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class FilterViewModel(
    private val defaultFilter: Filter? = null
) : IFilterViewModel {
    override val activeFilter: StateFlow<Filter?>
        field = MutableStateFlow(defaultFilter)

    override fun filterUpdated(newFilter: Filter?) {
        activeFilter.update { newFilter }
    }

    override fun resetFilter() {
        activeFilter.update { defaultFilter }
    }
}
