package com.feko.generictabletoprpg.feat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeatOverviewViewModel(
    private val getAllFeatsUseCase: GetAllFeatsUseCase
) : ViewModel() {
    private val _feats =
        MutableStateFlow<List<Feat>>(listOf())
    val searchString: StateFlow<String>
        get() = _searchString
    private var _searchString: MutableStateFlow<String> = MutableStateFlow("")
    val feats: Flow<List<Feat>>
        get() = combinedFeatFlow
    private val combinedFeatFlow: Flow<List<Feat>> =
        _feats.combine(_searchString) { feats, searchString ->
            feats.filter { feat ->
                feat.name.lowercase().contains(searchString.lowercase())
            }
        }

    init {
        viewModelScope.launch {
            val allFeats =
                withContext(Dispatchers.Default) {
                    getAllFeatsUseCase.invoke()
                }
            _feats.emit(allFeats)
        }
    }

    fun searchStringUpdated(searchString: String) {
        viewModelScope.launch {
            _searchString.emit(searchString)
        }
    }
}