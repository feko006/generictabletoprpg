package com.feko.generictabletoprpg.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.data.local.IGetAllDao
import com.feko.generictabletoprpg.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed
import com.feko.generictabletoprpg.features.filter.Filter
import com.feko.generictabletoprpg.features.searchall.domain.SmartNamedSearchComparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class OverviewViewModel<T : Any>(
    private val getAll: IGetAllDao<T>?
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

    private val _scrollToEndOfList = MutableStateFlow(false)
    val scrollToEndOfList: Flow<Boolean>
        get() = _scrollToEndOfList

    fun consumeScrollToEndOfListEvent() {
        viewModelScope.launch {
            _scrollToEndOfList.emit(false)
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

    protected suspend fun <R : Comparable<R>> addItem(item: T, sortedBy: (T) -> R?) {
        val items = _items.value.toMutableList()
        items.add(item)
        _items.emit(items.sortedBy(sortedBy))
        _scrollToEndOfList.emit(true)
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