package com.feko.generictabletoprpg.shared.features.filter

import com.feko.generictabletoprpg.shared.common.domain.model.IText

data class FilterChipData(
    val field: IText,
    val value: IText,
    var filterOnRemove: Filter?
)