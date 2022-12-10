package com.feko.generictabletoprpg.spell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.feko.generictabletoprpg.MainActivity.Companion.spells
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.spell.fiveetools.Spell

object SpellOverview : Navigation.Destination {
    override val route: String
        get() = "spellOverview"
    override val isRootDestination: Boolean
        get() = true
    override val screenTitle: String
        get() = "Spell Overview"

    override fun navHostComposable(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appBarTitle: MutableState<String>
    ) {
        navGraphBuilder.composable(route) {
            appBarTitle.value = screenTitle
            Screen(navController, spells)
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun Screen(
        navController: NavHostController,
        spells: List<Spell>
    ) {
        var searchString by rememberSaveable { mutableStateOf("") }
        Column {
            TextField(
                value = searchString,
                onValueChange = { searchString = it }
            )
            LazyColumn {
                items(spells.filter {
                    it.name!!.lowercase()
                        .contains(searchString.lowercase())
                }) { spell ->
                    Row(Modifier.clickable {
                        navController.navigate(SpellDetails.getNavRoute(spell.name!!))
                    }) {
                        Text(spell.name!!)
                    }
                }
            }
        }
    }
}