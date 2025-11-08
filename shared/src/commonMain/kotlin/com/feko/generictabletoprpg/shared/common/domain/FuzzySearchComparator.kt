package com.feko.generictabletoprpg.shared.common.domain

import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch

class FuzzySearchComparator<T>(private val searchString: String) : Comparator<T> {
    override fun compare(a: T, b: T): Int {
        val o1LowercaseName = (a as INamed).name.lowercase()
        val o2LowercaseName = (b as INamed).name.lowercase()
        val wRatio1 = FuzzySearch.weightedRatio(o1LowercaseName, searchString)
        val wRatio2 = FuzzySearch.weightedRatio(o2LowercaseName, searchString)
        return wRatio2.compareTo(wRatio1)
    }
}
