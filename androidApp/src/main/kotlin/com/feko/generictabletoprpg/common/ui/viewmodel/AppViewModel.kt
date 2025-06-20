package com.feko.generictabletoprpg.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.common.ui.RootDestinations
import com.feko.generictabletoprpg.features.basecontent.domain.usecase.ILoadBaseContentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
            _appState.emit(AppState.ShowingScreen.Default)
        }
    }

    fun set(
        appBarTitle: String? = null,
        navBarActions: List<ButtonState>? = null
    ) {
        viewModelScope.launch {
            var nextAppState = appState.value
            if (nextAppState is AppState.ShowingScreen) {
                nextAppState =
                    nextAppState.copy(
                        appBarTitle ?: nextAppState.appBarTitle,
                        navBarActions ?: nextAppState.navBarActions
                    )
                _appState.emit(nextAppState)
            }
        }
    }

    fun updateActiveDrawerItem(destination: RootDestinations) {
        viewModelScope.launch {
            _activeDrawerItemRoute.emit(destination.direction.route)
        }
    }

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
        data class ShowingScreen(
            val appBarTitle: String,
            val navBarActions: List<ButtonState>
        ) : AppState() {
            companion object {
                val Default = ShowingScreen("", listOf())
            }
        }
    }
}