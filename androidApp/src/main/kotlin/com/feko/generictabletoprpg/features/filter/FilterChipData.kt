package com.feko.generictabletoprpg.features.filter

import com.feko.generictabletoprpg.common.domain.model.IText

data class FilterChipData(
    val field: IText,
    val value: IText,
    var filterOnRemove: Filter?
)