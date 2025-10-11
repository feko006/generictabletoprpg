package com.feko.generictabletoprpg.shared.features.io.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.error_reading_file_toast
import com.feko.generictabletoprpg.failed_to_import_data_toast
import com.feko.generictabletoprpg.partially_imported_data_toast
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IImportAllUseCase
import com.feko.generictabletoprpg.successfully_imported_data_toast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class ImportViewModel(
    private val importAllUseCase: IImportAllUseCase
) : ViewModel() {
    val screenState: StateFlow<IImportScreenState>
        get() = _screenState
    private val _screenState =
        MutableStateFlow<IImportScreenState>(IImportScreenState.ReadyToImport)
    val toastMessage: SharedFlow<StringResource?>
        get() = _toastMessage
    private val _toastMessage: MutableSharedFlow<StringResource?> = MutableSharedFlow(0)

    fun fileSelected(contents: String?) {
        viewModelScope.launch {
            _screenState.emit(IImportScreenState.Importing)
            if (contents == null) {
                showToastAndResetScreen(Res.string.error_reading_file_toast)
                return@launch
            }
            val result: Result<Boolean> = importAllUseCase.import(contents)
            if (result.isFailure) {
                showToastAndResetScreen(Res.string.failed_to_import_data_toast)
            } else {
                val successfullyImported = result.getOrDefault(false)
                if (successfullyImported) {
                    showToastAndResetScreen(Res.string.successfully_imported_data_toast)
                } else {
                    showToastAndResetScreen(Res.string.partially_imported_data_toast)
                }
            }
        }
    }

    private suspend fun showToastAndResetScreen(toastMessage: StringResource) {
        _toastMessage.emit(toastMessage)
        _screenState.emit(IImportScreenState.RestartApp)
    }

    interface IImportScreenState {
        data object ReadyToImport : IImportScreenState
        data object Importing : IImportScreenState
        data object RestartApp : IImportScreenState
    }
}