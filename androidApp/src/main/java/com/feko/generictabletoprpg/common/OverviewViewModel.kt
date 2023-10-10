package com.feko.generictabletoprpg.common

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class OverviewViewModel<T>(
    private val getAll: IGetAll<T>?
) : ViewModel() {
    protected val _items = MutableStateFlow<List<T>>(listOf())
    val searchString: Flow<String>
        get() = _searchString
    protected var _searchString: MutableStateFlow<String> = MutableStateFlow("")
    val items: Flow<List<T>>
        get() = combinedItemFlow
    protected open val combinedItemFlow: Flow<List<T>> =
        _items.combine(_searchString) { items, searchString ->
            items
                .filter { item ->
                    item is Named
                            && item.name.lowercase().contains(searchString.lowercase())
                }
                .sortedWith(SmartNamedSearchComparator(searchString))
        }

    val isDialogVisible: Flow<Boolean>
        get() = _isDialogVisible
    protected val _isDialogVisible = MutableStateFlow(false)

    @StringRes
    var dialogTitleResource: Int = 0

    val isFabDropdownMenuExpanded: Flow<Boolean>
        get() = _isFabDropdownMenuExpanded
    protected val _isFabDropdownMenuExpanded = MutableStateFlow(false)

    init {
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

    fun hideDialog() {
        viewModelScope.launch {
            _isDialogVisible.emit(false)
        }
    }

    fun onDismissFabDropdownMenuRequested() {
        viewModelScope.launch {
            _isFabDropdownMenuExpanded.emit(false)
        }
    }

    fun toggleFabDropdownMenu() {
        viewModelScope.launch {
            _isFabDropdownMenuExpanded.emit(!_isFabDropdownMenuExpanded.value)
        }
    }

    protected suspend fun replaceItem(item: T) {
        if (item !is Identifiable) return
        val newList = _items.value.toMutableList()
        val index =
            newList.indexOfFirst {
                if (it !is Identifiable) false
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
                if (it !is Named) ""
                else it.name
            })
    }

    protected suspend fun removeItem(item: T) {
        if (item !is Identifiable) return
        val newList = _items.value.toMutableList()
        newList.removeAll {
            if (it !is Identifiable) false
            else it.id == item.id
        }
        _items.emit(newList)
    }
}