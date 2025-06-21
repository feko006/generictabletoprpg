package com.feko.generictabletoprpg.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.ui.RootDestinations
import com.feko.generictabletoprpg.features.basecontent.domain.usecase.ILoadBaseContentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel(
    loadBaseContentUseCase: ILoadBaseContentUseCase
) : ViewModel() {
    val refreshesPending: StateFlow<List<RootDestinations>>
        get() = _refreshesPending
    private val _refreshesPending: MutableStateFlow<List<RootDestinations>> =
        MutableStateFlow(listOf())
    val appState: StateFlow<AppState>
        get() = _appState
    private val _appState: MutableStateFlow<AppState> =
        MutableStateFlow(AppState.ImportingBaseContent)

    val activeDrawerItemRoute: StateFlow<String>
        get() = _activeDrawerItemRoute
    private val _activeDrawerItemRoute = MutableStateFlow("")

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                loadBaseContentUseCase.invoke()
            }
            _appState.emit(AppState.ShowingScreen)
        }
    }

    fun updateActiveDrawerItem(destination: RootDestinations) {
        _activeDrawerItemRoute.update { destination.direction.route }
    }

    // TODO: Just restart the app...
    @Deprecated("")
    fun contentImported() {
        viewModelScope.launch {
            _refreshesPending.emit(RootDestinations.Companion.refreshables())
        }
    }

    fun itemsRefreshed(destination: RootDestinations) {
        viewModelScope.launch {
            val newRefreshablesList =
                _refreshesPending.value
                    .minusElement(destination)
            _refreshesPending.emit(newRefreshablesList)
        }
    }

    sealed class AppState {
        data object ImportingBaseContent : AppState()
        data object ShowingScreen : AppState()
    }
}