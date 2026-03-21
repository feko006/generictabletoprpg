package com.feko.generictabletoprpg.shared.features.filter

import kotlin.reflect.KClass

open class CompositeFilter(
    var allowedTypes: List<KClass<*>>,
    type: KClass<*>?,
    name: String?
) : Filter(type, name) {
    override fun isAccepted(obj: Any, type: KClass<*>?): Boolean =
        allowedTypes.any { super.isAccepted(obj, it) }
}