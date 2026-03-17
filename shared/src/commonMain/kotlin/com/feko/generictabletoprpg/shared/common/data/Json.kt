package com.feko.generictabletoprpg.shared.common.data

import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition
import com.feko.generictabletoprpg.shared.features.armor.Armor
import com.feko.generictabletoprpg.shared.features.magicitem.MagicItemEntity
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentItem
import com.feko.generictabletoprpg.shared.features.tracker.model.IEquipmentItem
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val json = Json {
    classDiscriminator = "t"
    encodeDefaults = false
    explicitNulls = false
    ignoreUnknownKeys = true
    serializersModule = SerializersModule {
        polymorphic(IEquipmentItem::class) {
            subclass(Ammunition::class)
            subclass(Armor::class)
            subclass(MagicItemEntity::class)
            subclass(Weapon::class)
            subclass(EquipmentItem::class)
        }
    }
}
