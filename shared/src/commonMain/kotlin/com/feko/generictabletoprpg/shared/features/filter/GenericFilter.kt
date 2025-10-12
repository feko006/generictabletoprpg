package com.feko.generictabletoprpg.shared.features.filter

import kotlin.reflect.KClass

class GenericFilter(type: KClass<*>, name: String? = null) : Filter(type, name)
