package com.feko.generictabletoprpg.shared.features.io.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.failed_to_import_data_toast
import com.feko.generictabletoprpg.partially_imported_data_toast
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.IToastSubViewModel
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.ToastSubViewModel
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IImportAllUseCase
import com.feko.generictabletoprpg.successfully_imported_data_toast
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.flow.MutableStateFlow
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
    val toastMessage: IToastSubViewModel
        get() = _toastMessage
    private val _toastMessage = ToastSubViewModel(viewModelScope)

    fun fileSelected(file: PlatformFile?) {
        if (file == null) return
        viewModelScope.launch {
            _screenState.emit(IImportScreenState.Importing)
            val content = file.readString()
            val result: Result<Boolean> = importAllUseCase.import(content)
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
        _toastMessage.showMessage(toastMessage)
        _screenState.emit(IImportScreenState.RestartApp)
    }

    interface IImportScreenState {
        data object ReadyToImport : IImportScreenState
        data object Importing : IImportScreenState
        data object RestartApp : IImportScreenState
    }
}