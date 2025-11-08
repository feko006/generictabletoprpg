package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.export_failed
import com.feko.generictabletoprpg.export_location_hint
import com.feko.generictabletoprpg.export_successful
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.ToastMessage
import com.feko.generictabletoprpg.shared.logger
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ExportSubViewModel<T> : IExportSubViewModel<T> {
    protected var exportState: ExportState<T> = ExportState.None

    private var _toast = MutableStateFlow<ToastMessage?>(null)
    override val toast: Flow<ToastMessage?> = _toast

    override fun notifyCancelled() {
        exportState = ExportState.None
    }

    override suspend fun exportData(file: PlatformFile?) {
        if (file == null) {
            notifyFailed(NullPointerException("OutputStream is null."))
        } else {
            try {
                exportDataInternal(file)
                withContext(Dispatchers.Main) {
                    logger.debug { "Export successful." }
                    _toast.emit(
                        ToastMessage(Res.string.export_successful.asText(), _toast)
                    )
                }
            } catch (e: Exception) {
                notifyFailed(e)
            }
            exportState = ExportState.None
        }
    }

    abstract suspend fun exportDataInternal(file: PlatformFile)

    override fun notifyFailed(e: Exception) {
        CoroutineScope(Dispatchers.Main).launch {
            logger.error(e) { "Export failed." }
            _toast.emit(
                ToastMessage(Res.string.export_failed.asText(), _toast)
            )
            exportState = ExportState.None
        }
    }

    override fun exportAllRequested() {
        exportState = ExportState.ExportingAll
        _toast.update {
            ToastMessage(Res.string.export_location_hint.asText(), _toast)
        }
    }

    override fun exportSingleRequested(item: T) {
        exportState = ExportState.ExportingSingle(item)
        _toast.update {
            ToastMessage(Res.string.export_location_hint.asText(), _toast)
        }
    }
}