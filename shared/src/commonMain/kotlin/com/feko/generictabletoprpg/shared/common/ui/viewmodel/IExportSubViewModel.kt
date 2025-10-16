package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import io.github.vinceglb.filekit.PlatformFile

interface IExportSubViewModel<T> {
    val toast: IToastSubViewModel
    fun notifyCancelled()
    fun getExportedFileData(): String
    suspend fun exportData(file: PlatformFile?)
    fun notifyFailed(e: Exception)
    fun exportAllRequested()
    fun exportSingleRequested(item: T)
}