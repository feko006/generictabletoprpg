package com.feko.generictabletoprpg.export

import com.feko.generictabletoprpg.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.OutputStream

abstract class ExportViewModel<T> : IExportViewModel<T> {
    protected var exportState: ExportState<T> = ExportState.None

    override val exportToastMessage: SharedFlow<Int>
        get() = _exportToastMessage
    private val _exportToastMessage: MutableSharedFlow<Int> = MutableSharedFlow(0)

    override fun exportCancelled() {
        exportState = ExportState.None
    }

    override suspend fun exportData(outputStream: OutputStream?) {
        if (outputStream == null) {
            exportFailed(NullPointerException("OutputStream is null."))
        } else {
            try {
                exportDataInternal(outputStream)
                withContext(Dispatchers.Main) {
                    Timber.d("Export successful.")
                    _exportToastMessage.emit(R.string.export_successful)
                }
            } catch (e: Exception) {
                exportFailed(e)
            }
            exportState = ExportState.None
        }
    }

    abstract suspend fun exportDataInternal(outputStream: OutputStream)

    override fun exportFailed(e: Exception) {
        CoroutineScope(Dispatchers.Main).launch {
            Timber.e(e, "Export failed.")
            _exportToastMessage.emit(R.string.export_failed)
            exportState = ExportState.None
        }
    }

    override fun exportAllRequested() {
        CoroutineScope(Dispatchers.Main).launch {
            exportState = ExportState.ExportingAll
            _exportToastMessage.emit(R.string.export_location_hint)
        }
    }

    override fun exportSingleRequested(item: T) {
        CoroutineScope(Dispatchers.Main).launch {
            exportState = ExportState.ExportingSingle(item)
            _exportToastMessage.emit(R.string.export_location_hint)
        }
    }

    override fun exportToastMessageConsumed() {
        CoroutineScope(Dispatchers.Main).launch {
            _exportToastMessage.emit(0)
        }
    }
}