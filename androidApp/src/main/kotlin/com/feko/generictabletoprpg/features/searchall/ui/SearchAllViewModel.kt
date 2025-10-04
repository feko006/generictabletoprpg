package com.feko.generictabletoprpg.features.searchall.ui

import com.feko.generictabletoprpg.common.ui.viewmodel.FilterViewModel
import com.feko.generictabletoprpg.common.ui.viewmodel.IFilterViewModel
import com.feko.generictabletoprpg.common.ui.viewmodel.OverviewViewModel
import com.feko.generictabletoprpg.shared.features.filter.Filter
import com.feko.generictabletoprpg.shared.features.searchall.usecase.ISearchAllUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SearchAllViewModel(
    defaultFilter: Filter? = null,
    private val searchAllUseCase: ISearchAllUseCase,
    private val filterViewModel: FilterViewModel = FilterViewModel(defaultFilter)
) : OverviewViewModel<Any>(null),
    IFilterViewModel by filterViewModel {

    private val _isFilterBottomSheetVisible = MutableStateFlow(false)
    val isFilterBottomSheetVisible: Flow<Boolean> = _isFilterBottomSheetVisible

    fun filterRequested() {
        _isFilterBottomSheetVisible.update { true }
    }

    override fun getAllItems(): List<Any> = searchAllUseCase.getAllItems()

    override fun getFilterFlow(): StateFlow<Filter?> = activeFilter

    fun bottomSheetHidden() {
        _isFilterBottomSheetVisible.update { false }
    }
}