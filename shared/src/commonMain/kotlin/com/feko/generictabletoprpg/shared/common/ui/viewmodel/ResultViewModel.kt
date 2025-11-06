package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import androidx.lifecycle.ViewModel

class ResultViewModel<T> : ViewModel() {
    private var _selectionResult: T? = null
    val selectionResult
        get() = _selectionResult

    fun setSelectionResult(result: T) {
        _selectionResult = result
    }

    fun consumeSelectionResult() {
        _selectionResult = null
    }
}