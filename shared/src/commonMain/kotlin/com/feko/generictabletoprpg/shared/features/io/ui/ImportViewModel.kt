package com.feko.generictabletoprpg.shared.features.io.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dokar.sonner.ToasterDefaults
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.failed_to_import_data_toast
import com.feko.generictabletoprpg.nothing_to_import
import com.feko.generictabletoprpg.partially_imported_data_toast
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.ToastMessage
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IImportAllUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.ImportResult
import com.feko.generictabletoprpg.successfully_imported_data_toast
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImportViewModel(
    private val importAllUseCase: IImportAllUseCase
) : ViewModel() {

    val supportedExtensions = arrayOf(".json", "orcbrew")

    val screenState: StateFlow<IImportScreenState>
        field = MutableStateFlow<IImportScreenState>(IImportScreenState.ReadyToImport)
    val toastMessage: Flow<ToastMessage?>
        field = MutableStateFlow<ToastMessage?>(null)

    fun filesSelected(files: List<PlatformFile>) {
        viewModelScope.launch {
            screenState.emit(IImportScreenState.Importing)
            val results = files.map { file ->
                val content = file.readString()
                importAllUseCase.import(content)
            }

            val successes = results.count { it.isSuccess }
            if (successes == 0) {
                showToastAndResetScreen(Res.string.failed_to_import_data_toast.asText())
                return@launch
            }

            val nothingImported = results.all { res ->
                res.getOrNull()?.all { importRes ->
                    importRes.total == 0
                } ?: true
            }
            if (nothingImported) {
                showToastAndResetScreen(Res.string.nothing_to_import.asText())
                return@launch
            }

            var placeholderIndex = 1
            val importText = StringBuilder("%${placeholderIndex++}\$s\n")

            val message = when (successes) {
                results.size -> Res.string.successfully_imported_data_toast.asText()
                else -> Res.string.partially_imported_data_toast.asText()
            }

            val formatArgs = mutableListOf(message)
            results
                .map { it.getOrThrow() }
                .fold(listOf<ImportResult>()) { current, next ->
                    val results = current.toMutableList()
                    next.forEach { nextImportResult ->
                        val existingResult =
                            results.firstOrNull { it.type == nextImportResult.type }
                        if (existingResult != null) {
                            existingResult.successes += nextImportResult.successes
                            existingResult.total += nextImportResult.total
                        } else {
                            results.add(nextImportResult)
                        }
                    }
                    results
                }.filter { it.successes > 0 && it.total > 0 }
                .forEach {
                    importText.append("%${placeholderIndex++}\$s: ${it.successes} / ${it.total}\n")
                    formatArgs.add(it.type)
                }

            showToastAndResetScreen(importText.toString().asText(formatArgs))
        }
    }

    private suspend fun showToastAndResetScreen(toastMessage: IText) {
        this.toastMessage.emit(
            ToastMessage(
                toastMessage,
                this.toastMessage,
                ToasterDefaults.DurationLong
            )
        )
        screenState.emit(IImportScreenState.ReadyToImport)
    }

    interface IImportScreenState {
        data object ReadyToImport : IImportScreenState
        data object Importing : IImportScreenState
    }
}