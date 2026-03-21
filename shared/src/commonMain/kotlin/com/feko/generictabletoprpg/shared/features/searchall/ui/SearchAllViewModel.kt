package com.feko.generictabletoprpg.shared.features.searchall.ui

import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.shared.common.appTypes
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.FilterViewModel
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.IFilterViewModel
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.OverviewViewModel
import com.feko.generictabletoprpg.shared.features.filter.CompositeFilter
import com.feko.generictabletoprpg.shared.features.filter.Filter
import com.feko.generictabletoprpg.shared.features.filter.SpellFilter
import com.feko.generictabletoprpg.shared.features.searchall.usecase.ISearchAllUseCase
import com.feko.generictabletoprpg.shared.features.spell.Spell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.reflect.KClass

class SearchAllViewModel(
    defaultFilter: Filter? = null,
    private val searchAllUseCase: ISearchAllUseCase,
    private val filterViewModel: FilterViewModel = FilterViewModel(defaultFilter)
) : OverviewViewModel<Any>(null),
    IFilterViewModel by filterViewModel {

    private val _isFilterBottomSheetVisible = MutableStateFlow(false)
    val isFilterBottomSheetVisible: Flow<Boolean> = _isFilterBottomSheetVisible

    val availableTypeOptions: StateFlow<List<KClass<out Any>>> =
        activeFilter
            .map { filter ->
                when (filter) {
                    is CompositeFilter -> filter.allowedTypes
                    is SpellFilter -> listOf(Spell::class)
                    else -> appTypes.toList()
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    fun filterRequested() {
        _isFilterBottomSheetVisible.update { true }
    }

    override fun getAllItems(): Flow<List<Any>> = searchAllUseCase.getAllItems()

    override fun getFilterFlow(): StateFlow<Filter?> = activeFilter

    fun bottomSheetHidden() {
        _isFilterBottomSheetVisible.update { false }
    }
}