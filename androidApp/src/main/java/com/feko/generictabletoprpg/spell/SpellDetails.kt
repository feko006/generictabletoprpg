package com.feko.generictabletoprpg.spell

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.spell.fiveetools.Spell
import com.feko.generictabletoprpg.spell.fiveetools.toReadableString


object SpellDetails : Navigation.Destination {
    private const val spellIdArgument = "id"
    private const val routeBase = "spellDetails"

    override val route: String
        get() = "$routeBase/{$spellIdArgument}"
    override val isRootDestination: Boolean
        get() = false
    override val screenTitle: String
        get() = "Spell Details"

    fun getNavRoute(spellId: Long): String {
        return "$routeBase/$spellId"
    }

    override fun navHostComposable(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appBarTitle: MutableState<String>
    ) {
        navGraphBuilder.composable(
            route = route,
            arguments = listOf(navArgument(spellIdArgument) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val currentSpell = Spell()
            val spellId = backStackEntry.arguments?.getLong(spellIdArgument)
            appBarTitle.value = screenTitle
            Screen(currentSpell)
        }
    }

    @Composable
    private fun Screen(currentSpell: Spell) {
        Column {
            Text("Name: ${currentSpell.name}")
            Text("Level: ${currentSpell.level}")
            Text("School: ${currentSpell.school}")
            currentSpell.time.forEach {
                Text("Casting time: ${it.number} ${it.unit}")
            }
            currentSpell.range?.let {
                Text("Range: ${it.type?.capitalize()} ${it.distance}")
            }
            if (currentSpell.components?.any() == true) {
                Text("Components: ${currentSpell.components.toString()}")
            }
            currentSpell.duration.forEach { duration ->
                duration.duration?.let {
                    Text("Duration: ${it.amount} ${it.type}")
                }
                if (duration.duration == null) {
                    Text("Duration: ${duration.type}")
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Description:${currentSpell.entries.toReadableString()}")
            Spacer(Modifier.height(8.dp))
            Text(currentSpell.entriesHigherLevel.toReadableString())
        }
    }
}