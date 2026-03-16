package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition
import com.feko.generictabletoprpg.shared.features.ammunition.ui.AmmunitionDetailsContent
import com.feko.generictabletoprpg.shared.features.armor.Armor
import com.feko.generictabletoprpg.shared.features.armor.ui.ArmorDetailsContent
import com.feko.generictabletoprpg.shared.features.magicitem.MagicItem
import com.feko.generictabletoprpg.shared.features.magicitem.ui.MagicItemDetailsContent
import com.feko.generictabletoprpg.shared.features.tracker.model.IEquipmentItem
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import com.feko.generictabletoprpg.shared.features.weapon.ui.WeaponDetailsContent

@Composable
fun EquipmentItemDetailsScreen(
    equipmentItem: IEquipmentItem,
    onNavigationIconClick: () -> Unit
) {
    Scaffold(
        topBar = { GttrpgTopAppBar(equipmentItem.name.asText(), onNavigationIconClick) }
    ) { it: PaddingValues ->
        DetailsScreen(equipmentItem, Modifier.padding(it)) {
            when (equipmentItem) {
                is Ammunition -> AmmunitionDetailsContent(equipmentItem)
                is Armor -> ArmorDetailsContent(equipmentItem)
                is MagicItem -> MagicItemDetailsContent(equipmentItem)
                is Weapon -> WeaponDetailsContent(equipmentItem)
            }
        }
    }
}
