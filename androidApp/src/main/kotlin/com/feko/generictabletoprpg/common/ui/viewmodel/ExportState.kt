package com.feko.generictabletoprpg.common.ui.viewmodel

sealed class ExportState<out T> {
    data object None : ExportState<Nothing>()
    data class ExportingSingle<T>(val item: T) : ExportState<T>()
    data object ExportingAll : ExportState<Nothing>()
}