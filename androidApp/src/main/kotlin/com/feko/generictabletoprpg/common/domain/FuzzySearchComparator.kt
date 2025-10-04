package com.feko.generictabletoprpg.common.domain

import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch

class FuzzySearchComparator<T>(private val searchString: String) : Comparator<T> {
    override fun compare(o1: T?, o2: T?): Int {
        val o1LowercaseName = (o1 as INamed).name.lowercase()
        val o2LowercaseName = (o2 as INamed).name.lowercase()
        val wRatio1 = FuzzySearch.weightedRatio(o1LowercaseName, searchString)
        val wRatio2 = FuzzySearch.weightedRatio(o2LowercaseName, searchString)
        return wRatio2.compareTo(wRatio1)
    }
}
