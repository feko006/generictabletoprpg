package com.feko.generictabletoprpg.searchall

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.common.filter.IFilterViewModelExtension
import com.feko.generictabletoprpg.common.filter.IMutableFilterViewModelExtension
import com.feko.generictabletoprpg.filters.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchAllViewModel(
    private val searchAllUseCase: ISearchAllUseCase,
    private val filterViewModelExtension: IMutableFilterViewModelExtension
) : OverviewViewModel<Any>(null),
    IFilterViewModelExtension by filterViewModelExtension {

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible: Flow<Boolean> = _isBottomSheetVisible

    override val filterButtonVisible: Flow<Boolean> =
        items.map { it.any() }

    override fun filterRequested() {
        viewModelScope.launch {
            _isBottomSheetVisible.emit(true)
        }
    }

    override fun getAllItems(): List<Any> = searchAllUseCase.getAllItems()

    override fun getFilterFlow(): StateFlow<Filter?> = filterViewModelExtension.filter

    fun bottomSheetHidden() {
        viewModelScope.launch {
            _isBottomSheetVisible.emit(false)
        }
    }
}