package com.feko.generictabletoprpg.shared.common.domain

fun <T> createNewComparator(searchString: String): Comparator<T> =
    FuzzySearchComparator<T>(searchString)