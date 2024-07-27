package com.feko.generictabletoprpg.export

import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.toast.ToastSubViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.OutputStream

abstract class ExportSubViewModel<T> : IExportSubViewModel<T> {
    protected var exportState: ExportState<T> = ExportState.None

    private val _toast = ToastSubViewModel(CoroutineScope(Dispatchers.Main))
    override val toast = _toast

    override fun notifyCancelled() {
        exportState = ExportState.None
    }

    override suspend fun exportData(outputStream: OutputStream?) {
        if (outputStream == null) {
            notifyFailed(NullPointerException("OutputStream is null."))
        } else {
            try {
                exportDataInternal(outputStream)
                withContext(Dispatchers.Main) {
                    Timber.d("Export successful.")
                    _toast.showMessage(R.string.export_successful)
                }
            } catch (e: Exception) {
                notifyFailed(e)
            }
            exportState = ExportState.None
        }
    }

    abstract suspend fun exportDataInternal(outputStream: OutputStream)

    override fun notifyFailed(e: Exception) {
        CoroutineScope(Dispatchers.Main).launch {
            Timber.e(e, "Export failed.")
            _toast.showMessage(R.string.export_failed)
            exportState = ExportState.None
        }
    }

    override fun exportAllRequested() {
        CoroutineScope(Dispatchers.Main).launch {
            exportState = ExportState.ExportingAll
            _toast.showMessage(R.string.export_location_hint)
        }
    }

    override fun exportSingleRequested(item: T) {
        CoroutineScope(Dispatchers.Main).launch {
            exportState = ExportState.ExportingSingle(item)
            _toast.showMessage(R.string.export_location_hint)
        }
    }
}