package com.feko.generictabletoprpg.ui.spell

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.feko.generictabletoprpg.ui.Navigation
import com.feko.generictabletoprpg.ui.spell.fivee.Spell
import com.feko.generictabletoprpg.ui.spell.fivee.toReadableString

object SpellDetails : Navigation.IDestination {
    private const val nameArgument = "name"

    override val route: String
        get() = "spellDetails"
    override val arguments: List<NamedNavArgument>
        get() = listOf(navArgument(nameArgument) {
            type = NavType.StringType
        })

    fun getNameArgument(backStackEntry: NavBackStackEntry) =
        backStackEntry.arguments?.getString(nameArgument)

    @Composable
    fun Screen(currentSpell: Spell) {
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