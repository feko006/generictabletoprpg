package com.feko.generictabletoprpg.features.searchall.ui

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.ui.viewmodel.FilterSubViewModel
import com.feko.generictabletoprpg.common.ui.viewmodel.IFilterSubViewModel
import com.feko.generictabletoprpg.common.ui.viewmodel.OverviewViewModel
import com.feko.generictabletoprpg.features.filters.Filter
import com.feko.generictabletoprpg.features.searchall.domain.usecase.ISearchAllUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
        _isBottomSheetVisible.update { true }
    }

    override fun getAllItems(): List<Any> = searchAllUseCase.getAllItems()

    override fun getFilterFlow(): StateFlow<Filter?> = filter.activeFilter

    fun bottomSheetHidden() {
        _isBottomSheetVisible.update { false }
    }
}