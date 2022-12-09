package com.feko.generictabletoprpg.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.feko.generictabletoprpg.ui.spell.SpellDetails
import com.feko.generictabletoprpg.ui.spell.SpellOverview
import com.feko.generictabletoprpg.ui.spell.SpellSlots
import com.feko.generictabletoprpg.ui.spell.fivee.Spell

object Navigation {
    interface IDestination {
        val route: String
        val arguments: List<NamedNavArgument>
    }

    @Composable
    fun Host(navController: NavHostController, spells: List<Spell>) {
        NavHost(
            navController = navController,
            startDestination = SpellSlots.route
        ) {
            composable(SpellOverview.route) {
                SpellOverview.Screen(navController, spells)
            }
            composable(
                SpellDetails.route,
                arguments = SpellDetails.arguments
            ) { backStackEntry ->
                val currentSpell =
                    spells.first {
                        it.name == SpellDetails.getNameArgument(backStackEntry)
                    }

                SpellDetails.Screen(currentSpell)
            }
            composable(SpellSlots.route) {
                SpellSlots.Screen()
            }
        }
    }
}