package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.compose.SaverResultLauncher

interface IExportViewModel<T> {
    /** Updates state, shows a toast and launches the file saver. */
    fun export(item: T, fileSaverLauncher: SaverResultLauncher?)

    /** Updates state, shows a toast and launches the file saver. */
    fun exportAll(fileSaverLauncher: SaverResultLauncher?)

    /**
     * Prepares and writes data to the provided file, shows a toast depending on success or failure.
     */
    suspend fun onFileSaveLocationSelected(file: PlatformFile?)
}