package com.feko.generictabletoprpg.common

import com.feko.generictabletoprpg.tracker.TrackedThing

class SmartNamedSearchComparator<T>(private val searchString: String) : Comparator<T> {
    override fun compare(o1: T?, o2: T?): Int {
        val o1LowercaseName = (o1 as Named).name.lowercase()
        val o2LowercaseName = (o2 as Named).name.lowercase()
        val o1StartsWithSearchString = o1LowercaseName.startsWith(searchString)
        val o2StartsWithSearchString = o2LowercaseName.startsWith(searchString)
        val o1IsTrackedThing = o1 is TrackedThing
        val o2IsTrackedThing = o2 is TrackedThing
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
