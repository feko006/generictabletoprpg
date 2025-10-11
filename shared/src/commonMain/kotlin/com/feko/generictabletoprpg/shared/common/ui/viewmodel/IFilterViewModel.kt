package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import com.feko.generictabletoprpg.shared.features.filter.Filter
import kotlinx.coroutines.flow.StateFlow

interface IFilterViewModel {
    val activeFilter: StateFlow<Filter?>
    fun filterUpdated(newFilter: Filter?)
    fun resetFilter()
}