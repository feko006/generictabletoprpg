package com.feko.generictabletoprpg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.init.LoadBaseContentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel(
    loadBaseContentUseCase: LoadBaseContentUseCase
) : ViewModel() {
    val appState: StateFlow<AppState>
        get() = _appState
    private val _appState: MutableStateFlow<AppState> =
        MutableStateFlow(AppState.ImportingBaseContent)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                loadBaseContentUseCase.invoke()
            }
            _appState.emit(AppState.ReadyToUse)
        }
    }

    sealed class AppState {
        object ImportingBaseContent : AppState()
        object ReadyToUse : AppState()
    }
}