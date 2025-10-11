package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.export_failed
import com.feko.generictabletoprpg.export_location_hint
import com.feko.generictabletoprpg.export_successful
import com.feko.generictabletoprpg.shared.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                    logger.debug { "Export successful." }
                    _toast.showMessage(Res.string.export_successful)
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
            logger.error(e) { "Export failed." }
            _toast.showMessage(Res.string.export_failed)
            exportState = ExportState.None
        }
    }

    override fun exportAllRequested() {
        CoroutineScope(Dispatchers.Main).launch {
            exportState = ExportState.ExportingAll
            _toast.showMessage(Res.string.export_location_hint)
        }
    }

    override fun exportSingleRequested(item: T) {
        CoroutineScope(Dispatchers.Main).launch {
            exportState = ExportState.ExportingSingle(item)
            _toast.showMessage(Res.string.export_location_hint)
        }
    }
}