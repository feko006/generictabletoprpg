package com.feko.generictabletoprpg.shared.features.filter

import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition
import com.feko.generictabletoprpg.shared.features.armor.Armor
import com.feko.generictabletoprpg.shared.features.magicitem.MagicItem
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import kotlin.reflect.KClass

class EquipmentFilter(
    type: KClass<*>? = null,
    name: String? = null
) : CompositeFilter(
    listOf(
        Ammunition::class,
        Armor::class,
        Weapon::class,
        MagicItem::class
    ),
    type,
    name
)