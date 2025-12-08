package com.feko.generictabletoprpg.shared.features.magicitem

import com.feko.generictabletoprpg.shared.common.domain.model.IFromSource
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.IKClassProvider
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import kotlin.reflect.KClass

interface MagicItem : IIdentifiable, INamed, IFromSource, IKClassProvider {
    val type: String
    val rarity: String
    val description: String
    override val kclass: KClass<*>
        get() = MagicItem::class
}