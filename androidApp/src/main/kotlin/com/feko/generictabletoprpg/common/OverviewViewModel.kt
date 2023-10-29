package com.feko.generictabletoprpg.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.filter.FilterPredicate
import com.feko.generictabletoprpg.filters.Filter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class OverviewViewModel<T : Any>(
    private val getAll: IGetAll<T>?
) : ViewModel() {
    protected val _items = MutableStateFlow<List<T>>(listOf())
    val searchString: Flow<String>
        get() = _searchString
    protected var _searchString: MutableStateFlow<String> = MutableStateFlow("")
    val items: Flow<List<T>>
        get() = combinedItemFlow
    protected open val combinedItemFlow: Flow<List<T>> by lazy {
        var itemsToSearchThrough: Flow<List<T>> = _items
        val filterFlow = getFilterFlow()
        if (filterFlow != null) {
            itemsToSearchThrough =
                itemsToSearchThrough.combine(filterFlow) { items, filter ->
                    items.filter(FilterPredicate(filter))
                }
        }
        itemsToSearchThrough.combine(_searchString) { items, searchString ->
            items
                .filter { item ->
                    item is INamed
                            && item.name.lowercase().contains(searchString.lowercase())
                }
                .sortedWith(SmartNamedSearchComparator(searchString))
        }
    }

    open fun getFilterFlow(): StateFlow<Filter?>? = null

    init {
        refreshItems()
    }

    fun refreshItems() {
        viewModelScope.launch {
            val allItems =
                withContext(Dispatchers.Default) {
                    getAllItems()
                }
            _items.emit(allItems)
        }
    }

    open fun getAllItems(): List<T> = getAll!!.getAllSortedByName()

    fun searchStringUpdated(searchString: String) {
        viewModelScope.launch {
            _searchString.emit(searchString)
        }
    }

    protected suspend fun replaceItem(item: T) {
        if (item !is IIdentifiable) return
        val newList = _items.value.toMutableList()
        val index =
            newList.indexOfFirst {
                if (it !is IIdentifiable) false
                else it.id == item.id
            }
        newList.removeAt(index)
        newList.add(index, item)
        _items.emit(newList)
    }

    protected suspend fun addItem(item: T) {
        val items = _items.value.toMutableList()
        items.add(item)
        _items.emit(
            items.sortedBy {
                if (it !is INamed) ""
                else it.name
            })
    }

    protected suspend fun removeItem(item: T) {
        if (item !is IIdentifiable) return
        val newList = _items.value.toMutableList()
        newList.removeAll {
            if (it !is IIdentifiable) false
            else it.id == item.id
        }
        _items.emit(newList)
    }
}