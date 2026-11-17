package com.feko.generictabletoprpg.shared.common.ui

import com.dokar.sonner.ToasterDefaults
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Duration

data class ToastMessage(
    val message: IText,
    val duration: Duration = ToasterDefaults.DurationDefault,
    val toastConsumed: () -> Unit
) {
    constructor(
        message: IText,
        flow: MutableStateFlow<ToastMessage?>,
        duration: Duration = ToasterDefaults.DurationDefault
    ) : this(message, toastConsumed = { flow.update { null } })
}
