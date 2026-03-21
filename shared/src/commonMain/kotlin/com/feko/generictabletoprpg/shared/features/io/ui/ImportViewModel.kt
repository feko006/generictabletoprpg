package com.feko.generictabletoprpg.shared.features.io.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.failed_to_import_data_toast
import com.feko.generictabletoprpg.partially_imported_data_toast
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.ToastMessage
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IImportAllUseCase
import com.feko.generictabletoprpg.successfully_imported_data_toast
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class ImportViewModel(
    private val importAllUseCase: IImportAllUseCase
) : ViewModel() {

    val supportedExtensions = arrayOf(".json", "orcbrew")

    val screenState: StateFlow<IImportScreenState>
        get() = _screenState
    private val _screenState =
        MutableStateFlow<IImportScreenState>(IImportScreenState.ReadyToImport)
    private val _toastMessage = MutableStateFlow<ToastMessage?>(null)
    val toastMessage: Flow<ToastMessage?> = _toastMessage

    fun filesSelected(files: List<PlatformFile>) {
        viewModelScope.launch {
            _screenState.emit(IImportScreenState.Importing)
            val result = files.map { file ->
                val content = file.readString()
                importAllUseCase.import(content)
            }
            val successes = result.count { it.isSuccess && it.getOrDefault(false) }
            when (successes) {
                0 -> showToastAndResetScreen(Res.string.failed_to_import_data_toast)
                result.size -> showToastAndResetScreen(Res.string.successfully_imported_data_toast)
                else -> showToastAndResetScreen(Res.string.partially_imported_data_toast)
            }
        }
    }

    private suspend fun showToastAndResetScreen(toastMessage: StringResource) {
        _toastMessage.emit(ToastMessage(toastMessage.asText(), _toastMessage))
        _screenState.emit(IImportScreenState.ReadyToImport)
    }

    interface IImportScreenState {
        data object ReadyToImport : IImportScreenState
        data object Importing : IImportScreenState
    }
}