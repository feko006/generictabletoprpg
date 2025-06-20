package com.feko.generictabletoprpg.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.data.local.IGetByIdDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class DetailsViewModel<T>(
    private val getById: IGetByIdDao<T>
) : ViewModel() {
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

    fun itemIdChanged(id: Long) {
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

    open fun getItemById(id: Long): T = getById.getById(id)

    sealed class DetailsScreenState {
        data object Loading : DetailsScreenState()
        class ItemReady<T>(val item: T) : DetailsScreenState()
    }
}