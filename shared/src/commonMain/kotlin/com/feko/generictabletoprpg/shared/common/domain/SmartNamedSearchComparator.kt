package com.feko.generictabletoprpg.shared.common.domain

import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing

class SmartNamedSearchComparator<T>(private val searchString: String) : Comparator<T> {
    override fun compare(a: T, b: T): Int {
        val o1LowercaseName = (a as INamed).name.lowercase()
        val o2LowercaseName = (b as INamed).name.lowercase()
        val o1StartsWithSearchString = o1LowercaseName.startsWith(searchString.lowercase())
        val o2StartsWithSearchString = o2LowercaseName.startsWith(searchString.lowercase())
        val o1IsTrackedThing = a is TrackedThing
        val o2IsTrackedThing = b is TrackedThing
        return if (o1IsTrackedThing xor o2IsTrackedThing) {
            if (o1IsTrackedThing) {
                -1
            } else {
                1
            }
        } else {
            if (o1StartsWithSearchString xor o2StartsWithSearchString) {
                if (o1StartsWithSearchString) {
                    -1
                } else {
                    1
                }
            } else {
                o1LowercaseName.compareTo(o2LowercaseName)
            }
        }
    }
}