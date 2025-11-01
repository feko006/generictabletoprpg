package com.feko.generictabletoprpg.shared.common.ui

import com.feko.generictabletoprpg.shared.common.domain.model.IText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class ToastMessage(
    val message: IText,
    val toastConsumed: () -> Unit
) {
    constructor(message: IText, flow: MutableStateFlow<ToastMessage?>)
            : this(message, { flow.update { null } })
}
