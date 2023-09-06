package com.feko.generictabletoprpg.searchall

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.feko.generictabletoprpg.AppViewModel
import com.feko.generictabletoprpg.action.Action
import com.feko.generictabletoprpg.action.ActionDetails
import com.feko.generictabletoprpg.ammunition.Ammunition
import com.feko.generictabletoprpg.ammunition.AmmunitionDetails
import com.feko.generictabletoprpg.armor.Armor
import com.feko.generictabletoprpg.armor.ArmorDetails
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.OverviewListItem
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.composable.OverviewScreen
import com.feko.generictabletoprpg.common.Identifiable
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

@Composable
fun SearchAllScreen(
    navHostController: NavHostController,
    appViewModel: AppViewModel
) {
    appViewModel
        .set(
            appBarTitle = "Search All",
            navBarActions = listOf()
        )
    OverviewScreen<SearchAllViewModel, Any>(
        navHostController,
        koinViewModel(),
        listItem = { item, navController ->
            OverviewListItem(
                item = item,
                navController = navController,
                getNavRoute = ::getNavRouteInternal
            )
        },
        uniqueListItemKey = { "${it::class}${(it as Identifiable).id}" }
    )
}

fun getNavRouteInternal(item: Any): String {
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