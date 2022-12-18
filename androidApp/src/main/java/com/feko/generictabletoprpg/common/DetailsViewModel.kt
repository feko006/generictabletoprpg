package com.feko.generictabletoprpg.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class DetailsViewModel<T> : ViewModel() {
    val screenState: Flow<DetailsScreenState>
        get() = _item.map { item ->
            if (item == null) {
                DetailsScreenState.Loading
            } else {
                DetailsScreenState.ItemReady(item)
            }
        }
    private val _item: MutableStateFlow<T?> =
        MutableStateFlow(null)
    private var _id: Long = -1

    fun featIdChanged(id: Long) {
        if (_id != id) {
            viewModelScope.launch {
                _id = id
                val item =
                    withContext(Dispatchers.Default) {
                        getItemById(_id)
                    }
                _item.emit(item)
            }
        }
    }

    abstract fun getItemById(id: Long): T

    sealed class DetailsScreenState {
        object Loading : DetailsScreenState()
        class ItemReady<T>(val item: T) : DetailsScreenState()
    }
}