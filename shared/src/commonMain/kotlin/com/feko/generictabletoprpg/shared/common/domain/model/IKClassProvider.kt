package com.feko.generictabletoprpg.shared.common.domain.model

import kotlin.reflect.KClass

interface IKClassProvider {
    val kclass: KClass<*>
}