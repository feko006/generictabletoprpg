package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import com.feko.generictabletoprpg.shared.common.ui.ToastMessage
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.flow.Flow

interface IExportSubViewModel<T> {
    val toast: Flow<ToastMessage?>
    fun notifyCancelled()
    fun getExportedFileData(): String
    suspend fun exportData(file: PlatformFile?)
    fun notifyFailed(e: Exception)
    fun exportAllRequested()
    fun exportSingleRequested(item: T)
}