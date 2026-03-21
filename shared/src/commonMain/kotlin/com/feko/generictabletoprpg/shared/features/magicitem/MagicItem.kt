package com.feko.generictabletoprpg.shared.features.magicitem

import com.feko.generictabletoprpg.shared.common.domain.model.IFromSource
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.IKClassProvider
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.shared.features.tracker.model.IEquipmentItem
import kotlin.reflect.KClass

interface MagicItem : IIdentifiable, INamed, IFromSource, IKClassProvider, IEquipmentItem {
    val type: String
    val rarity: String
    val attunement: Boolean
    val description: String
    override val kclass: KClass<*>
        get() = MagicItem::class
}