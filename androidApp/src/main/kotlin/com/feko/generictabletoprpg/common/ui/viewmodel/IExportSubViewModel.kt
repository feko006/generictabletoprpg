package com.feko.generictabletoprpg.common.ui.viewmodel

import java.io.OutputStream

interface IExportSubViewModel<T> {
    val toast: IToastSubViewModel
    fun notifyCancelled()
    fun getExportedFileData(): Pair<String, String>
    suspend fun exportData(outputStream: OutputStream?)
    fun notifyFailed(e: Exception)
    fun exportAllRequested()
    fun exportSingleRequested(item: T)
}