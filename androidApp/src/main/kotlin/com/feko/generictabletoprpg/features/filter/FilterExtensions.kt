package com.feko.generictabletoprpg.features.filter

fun Filter.index(): Int =
    when (this) {
        is GenericFilter -> 0
        is SpellFilter -> 1
    }

fun Int.asFilter(): Filter =
    when (this) {
        0 -> GenericFilter(Any::class.java)
        1 -> SpellFilter()
        else -> throw IllegalStateException()
    }