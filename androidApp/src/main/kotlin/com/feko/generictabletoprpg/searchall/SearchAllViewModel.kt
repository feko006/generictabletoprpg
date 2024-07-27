package com.feko.generictabletoprpg.searchall

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.common.filter.FilterSubViewModel
import com.feko.generictabletoprpg.common.filter.IFilterSubViewModel
import com.feko.generictabletoprpg.filters.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchAllViewModel(
    defaultFilter: Filter? = null,
    private val searchAllUseCase: ISearchAllUseCase
) : OverviewViewModel<Any>(null) {

    private val _filter = FilterSubViewModel(viewModelScope, defaultFilter)
    val filter: IFilterSubViewModel = _filter

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible: Flow<Boolean> = _isBottomSheetVisible

    init {
        viewModelScope.launch {
            _items.collect {
                _filter._isButtonVisible.emit(it.any())
            }
        }
    }

    fun filterRequested() {
        viewModelScope.launch {
            _isBottomSheetVisible.emit(true)
        }
    }

    override fun getAllItems(): List<Any> = searchAllUseCase.getAllItems()

    override fun getFilterFlow(): StateFlow<Filter?> = filter.activeFilter

    fun bottomSheetHidden() {
        viewModelScope.launch {
            _isBottomSheetVisible.emit(false)
        }
    }
}