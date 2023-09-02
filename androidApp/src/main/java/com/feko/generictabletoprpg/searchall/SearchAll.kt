package com.feko.generictabletoprpg.searchall

import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.action.Action
import com.feko.generictabletoprpg.action.ActionDetails
import com.feko.generictabletoprpg.ammunition.Ammunition
import com.feko.generictabletoprpg.ammunition.AmmunitionDetails
import com.feko.generictabletoprpg.armor.Armor
import com.feko.generictabletoprpg.armor.ArmorDetails
import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.OverviewScreen
import com.feko.generictabletoprpg.condition.Condition
import com.feko.generictabletoprpg.condition.ConditionDetails
import com.feko.generictabletoprpg.disease.Disease
import com.feko.generictabletoprpg.disease.DiseaseDetails
import com.feko.generictabletoprpg.feat.Feat
import com.feko.generictabletoprpg.feat.FeatDetails
import com.feko.generictabletoprpg.spell.Spell
import com.feko.generictabletoprpg.spell.SpellDetails
import com.feko.generictabletoprpg.weapon.Weapon
import com.feko.generictabletoprpg.weapon.WeaponDetails
import org.koin.androidx.compose.koinViewModel

object SearchAll
    : OverviewScreen<SearchAllViewModel, Any>(
    object : Navigation.DetailsNavRouteProvider {
        override fun getNavRoute(id: Long): String {
            throw UnsupportedOperationException(
                "Details nav route provider should not be used from the search all screen."
            )
        }
    },
    "Search All",
    "searchall"
) {
    @Composable
    override fun getViewModel(): SearchAllViewModel = koinViewModel()

    override fun uniqueListItemKey(listItem: Any): Any =
        "${listItem::class}${(listItem as Identifiable).id}"

    override fun getNavRouteInternal(item: Any): String {
        val id = (item as Identifiable).id
        return when (item) {
            is Spell -> SpellDetails.getNavRoute(id)
            is Feat -> FeatDetails.getNavRoute(id)
            is Action -> ActionDetails.getNavRoute(id)
            is Condition -> ConditionDetails.getNavRoute(id)
            is Disease -> DiseaseDetails.getNavRoute(id)
            is Weapon -> WeaponDetails.getNavRoute(id)
            is Ammunition -> AmmunitionDetails.getNavRoute(id)
            is Armor -> ArmorDetails.getNavRoute(id)
            else -> throw IllegalStateException("Unknown list item")
        }
    }
}

