package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class OverviewViewModel<T> : ViewModel()
        where T : Named {
    private val _items =
        MutableStateFlow<List<T>>(listOf())
    val searchString: StateFlow<String>
        get() = _searchString
    private var _searchString: MutableStateFlow<String> = MutableStateFlow("")
    val items: Flow<List<T>>
        get() = combinedItemFlow
    private val combinedItemFlow: Flow<List<T>> =
        _items.combine(_searchString) { feats, searchString ->
            feats.filter { feat ->
                feat.name.lowercase().contains(searchString.lowercase())
            }
        }

    init {
        viewModelScope.launch {
            val allItems =
                withContext(Dispatchers.Default) {
                    getAllItems()
                }
            _items.emit(allItems)
        }
    }

    abstract fun getAllItems(): List<T>

    fun searchStringUpdated(searchString: String) {
        viewModelScope.launch {
            _searchString.emit(searchString)
        }
    }
}