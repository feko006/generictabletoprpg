package com.feko.generictabletoprpg.export

import kotlinx.coroutines.flow.SharedFlow
import java.io.OutputStream

interface IExportViewModel<T> {
    val exportToastMessage: SharedFlow<Int>
    fun exportCancelled()
    fun getExportedFileData(): Pair<String, String>
    suspend fun exportData(outputStream: OutputStream?)
    fun exportFailed(e: Exception)
    fun exportAllRequested()
    fun exportSingleRequested(item: T)
}