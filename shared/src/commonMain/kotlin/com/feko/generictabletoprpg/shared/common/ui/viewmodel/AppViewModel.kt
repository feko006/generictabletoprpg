package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination
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
        field = MutableStateFlow<AppState>(AppState.ImportingBaseContent)
    val activeDrawerItemRoute: StateFlow<INavigationDestination>
        field = MutableStateFlow(INavigationDestination.startDestination)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                loadBaseContentUseCase.invoke()
            }
            appState.emit(AppState.ShowingScreen)
        }
    }

    fun updateActiveDrawerItem(destination: INavigationDestination) {
        activeDrawerItemRoute.update { destination }
    }

    sealed class AppState {
        data object ImportingBaseContent : AppState()
        data object ShowingScreen : AppState()
    }
}