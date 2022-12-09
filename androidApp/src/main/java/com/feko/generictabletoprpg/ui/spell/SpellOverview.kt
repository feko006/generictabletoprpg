package com.feko.generictabletoprpg.ui.spell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import com.feko.generictabletoprpg.ui.Navigation
import com.feko.generictabletoprpg.ui.spell.fivee.Spell

object SpellOverview : Navigation.IDestination {
    override val route: String
        get() = "spellOverview"

    override val arguments: List<NamedNavArgument>
        get() = listOf()

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun Screen(
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
                        navController.navigate("details/${spell.name!!}")
                    }) {
                        Text(spell.name!!)
                    }
                }
            }
        }
    }
}