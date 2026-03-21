package com.feko.generictabletoprpg.shared.features.filter

fun Filter.index(): Int =
    when (this) {
        is GenericFilter -> 0
        is SpellFilter -> 1
        is EquipmentFilter -> 2
        else -> throw IllegalStateException()
    }

fun Int.asFilter(): Filter =
    when (this) {
        0 -> GenericFilter(Any::class)
        1 -> SpellFilter()
        2 -> EquipmentFilter()
        else -> throw IllegalStateException()
    }