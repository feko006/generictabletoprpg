package com.feko.generictabletoprpg.feat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeatDetailsViewModel(
    private val getFeatByIdUseCase: GetFeatByIdUseCase
) : ViewModel() {
    val screenState: Flow<FeatDetailsScreenState>
        get() = _feat.map { feat ->
            if (feat == null) {
                FeatDetailsScreenState.Loading
            } else {
                FeatDetailsScreenState.FeatReady(feat)
            }
        }
    private val _feat: MutableStateFlow<Feat?> =
        MutableStateFlow(null)
    private var _featId: Long = -1

    fun featIdChanged(featId: Long) {
        if (_featId != featId) {
            viewModelScope.launch {
                _featId = featId
                val newFeat =
                    withContext(Dispatchers.Default) {
                        getFeatByIdUseCase.getById(_featId)
                    }
                _feat.emit(newFeat)
            }
        }
    }

    sealed class FeatDetailsScreenState {
        object Loading : FeatDetailsScreenState()
        class FeatReady(val feat: Feat) : FeatDetailsScreenState()
    }
}