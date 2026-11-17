package com.feko.generictabletoprpg.shared.features.io.domain.usecase

import com.feko.generictabletoprpg.shared.common.domain.model.IText

data class ImportResult(
    val type: IText,
    var successes: Int,
    var total: Int
)