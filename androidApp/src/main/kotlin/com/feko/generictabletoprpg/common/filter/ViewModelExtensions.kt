package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.filter.IFilterViewModelExtension
import com.feko.generictabletoprpg.filters.Filter
import kotlinx.coroutines.launch


fun <T> T.filterUpdateRequested(newFilter: Filter)
        where T : ViewModel,
              T : IFilterViewModelExtension {
    viewModelScope.launch {
        filterUpdated(newFilter)
    }
}
