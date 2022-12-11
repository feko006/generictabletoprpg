package com.feko.generictabletoprpg.spell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.common.Common
import org.koin.androidx.compose.koinViewModel

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
            Screen(navController)
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun Screen(
        navController: NavHostController
    ) {
        val viewModel: SpellOverviewViewModel = koinViewModel()
        val spells by viewModel.spells.collectAsState(listOf())
        val searchString by viewModel.searchString.collectAsState("")
        Column(Modifier.padding(8.dp)) {
            Common.SearchTextField(
                searchString
            ) {
                viewModel.searchStringUpdated(it)
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                Modifier.fillMaxSize()
            ) {
                items(spells.filter {
                    it.name.lowercase()
                        .contains(searchString.lowercase())
                }, key = { it.id }
                ) { spell ->
                    ListItem(
                        headlineText = {
                            Text(spell.name)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(SpellDetails.getNavRoute(spell.id))
                            })
                }
            }
        }
    }
}