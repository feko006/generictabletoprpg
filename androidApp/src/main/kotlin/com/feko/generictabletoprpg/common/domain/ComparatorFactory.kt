package com.feko.generictabletoprpg.common.domain

fun <T> createNewComparator(searchString: String): Comparator<T> =
    FuzzySearchComparator<T>(searchString)