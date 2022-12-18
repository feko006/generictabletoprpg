package com.feko.generictabletoprpg.import

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImportViewModel(
    private val importAllUseCase: ImportAllUseCase
) : ViewModel() {
    val screenState: StateFlow<ImportScreenState>
        get() = _screenState
    private val _screenState = MutableStateFlow<ImportScreenState>(ImportScreenState.ReadyToImport)
    val toastMessage: SharedFlow<String>
        get() = _toastMessage
    private val _toastMessage = MutableSharedFlow<String>()

    fun fileSelected(contents: String?) {
        viewModelScope.launch {
            _screenState.emit(ImportScreenState.Importing)
            if (contents == null) {
                showToastAndResetScreen("Error reading file")
                return@launch
            }
            val result: Result<Boolean> =
                withContext(Dispatchers.Default) {
                    importAllUseCase.import(contents)
                }
            if (result.isFailure) {
                showToastAndResetScreen("Failed to import data")
            } else {
                val successfullyImported = result.getOrDefault(false)
                if (successfullyImported) {
                    showToastAndResetScreen("Successfully imported all data")
                } else {
                    showToastAndResetScreen("Data partially imported")
                }
            }
        }
    }

    private suspend fun showToastAndResetScreen(toastMessage: String) {
        _toastMessage.emit(toastMessage)
        _screenState.emit(ImportScreenState.ReadyToImport)
    }

    sealed class ImportScreenState {
        object ReadyToImport : ImportScreenState()
        object Importing : ImportScreenState()
    }
}
