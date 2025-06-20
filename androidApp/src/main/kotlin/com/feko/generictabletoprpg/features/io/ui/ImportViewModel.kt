package com.feko.generictabletoprpg.features.io.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.features.io.domain.usecase.IImportAllUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImportViewModel(
    private val importAllUseCase: IImportAllUseCase
) : ViewModel() {
    val screenState: StateFlow<ImportScreenState>
        get() = _screenState
    private val _screenState = MutableStateFlow<ImportScreenState>(ImportScreenState.ReadyToImport)
    val toastMessage: SharedFlow<Int>
        get() = _toastMessage
    private val _toastMessage: MutableSharedFlow<Int> = MutableSharedFlow(0)

    fun fileSelected(contents: String?) {
        viewModelScope.launch {
            _screenState.emit(ImportScreenState.Importing)
            if (contents == null) {
                showToastAndResetScreen(R.string.error_reading_file_toast)
                return@launch
            }
            val result: Result<Boolean> =
                withContext(Dispatchers.Default) {
                    importAllUseCase.import(contents)
                }
            if (result.isFailure) {
                showToastAndResetScreen(R.string.failed_to_import_data_toast)
            } else {
                val successfullyImported = result.getOrDefault(false)
                if (successfullyImported) {
                    showToastAndResetScreen(R.string.successfully_imported_data_toast)
                } else {
                    showToastAndResetScreen(R.string.partially_imported_data_toast)
                }
            }
        }
    }

    private suspend fun showToastAndResetScreen(@StringRes toastMessage: Int) {
        _toastMessage.emit(toastMessage)
        _screenState.emit(ImportScreenState.ReadyToImport)
    }

    sealed class ImportScreenState {
        data object ReadyToImport : ImportScreenState()
        data object Importing : ImportScreenState()
    }
}
