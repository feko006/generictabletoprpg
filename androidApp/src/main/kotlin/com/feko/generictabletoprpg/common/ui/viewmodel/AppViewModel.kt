package com.feko.generictabletoprpg.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.ui.components.INavigationDestination
import com.feko.generictabletoprpg.shared.features.basecontent.domain.usecase.ILoadBaseContentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel(
    loadBaseContentUseCase: ILoadBaseContentUseCase
) : ViewModel() {
    val appState: StateFlow<AppState>
        get() = _appState
    private val _appState: MutableStateFlow<AppState> =
        MutableStateFlow(AppState.ImportingBaseContent)

    val activeDrawerItemRoute: StateFlow<INavigationDestination>
        get() = _activeDrawerItemRoute
    private val _activeDrawerItemRoute = MutableStateFlow(INavigationDestination.startDestination)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                loadBaseContentUseCase.invoke()
            }
            _appState.emit(AppState.ShowingScreen)
        }
    }

    fun updateActiveDrawerItem(destination: INavigationDestination) {
        _activeDrawerItemRoute.update { destination }
    }

    sealed class AppState {
        data object ImportingBaseContent : AppState()
        data object ShowingScreen : AppState()
    }
}