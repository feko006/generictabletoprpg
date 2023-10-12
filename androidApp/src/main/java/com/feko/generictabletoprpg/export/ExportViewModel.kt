package com.feko.generictabletoprpg.export

import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ILogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream

abstract class ExportViewModel<T>(
    private val logger: ILogger
) : IExportViewModel<T> {
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
                    logger.debug("Export successful.")
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
            logger.error(e, "Export failed.")
            _exportToastMessage.emit(R.string.export_failed)
            exportState = ExportState.None
        }
    }

    override fun exportAllRequested() {
        exportState = ExportState.ExportingAll
    }

    override fun exportSingleRequested(item: T) {
        exportState = ExportState.ExportingSingle(item)
    }
}