package com.feko.generictabletoprpg.common.filter

import com.feko.generictabletoprpg.filters.Filter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FilterSubViewModel(
    private val coroutineScope: CoroutineScope
) : IFilterSubViewModel {
    override val activeFilter: StateFlow<Filter?>
        get() = _activeFilter
    val _activeFilter = MutableStateFlow<Filter?>(null)
    override val isButtonVisible: Flow<Boolean>
        get() = _isButtonVisible
    override val offButtonVisible: Flow<Boolean> = activeFilter.map { it != null }
    val _isButtonVisible = MutableStateFlow(false)

    override fun filterUpdated(newFilter: Filter?) {
        coroutineScope.launch {
            _activeFilter.emit(newFilter)
        }
    }

    override fun resetFilter() {
        coroutineScope.launch {
            _activeFilter.emit(null)
        }
    }
}
