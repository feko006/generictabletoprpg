package com.feko.generictabletoprpg.export

import kotlinx.coroutines.flow.SharedFlow
import java.io.OutputStream

interface IExportSubViewModel<T> {
    val toastMessage: SharedFlow<Int>
    fun notifyCancelled()
    fun getExportedFileData(): Pair<String, String>
    suspend fun exportData(outputStream: OutputStream?)
    fun notifyFailed(e: Exception)
    fun exportAllRequested()
    fun exportSingleRequested(item: T)
    fun toastMessageConsumed()
}