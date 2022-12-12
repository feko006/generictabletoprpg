package com.feko.generictabletoprpg.feat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.common.Common
import org.koin.androidx.compose.koinViewModel

object FeatOverview : Navigation.Destination {
    override val screenTitle: String
        get() = "Feats"
    override val route: String
        get() = "featOverview"
    override val isRootDestination: Boolean
        get() = true

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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(
        navController: NavHostController
    ) {
        val viewModel: FeatOverviewViewModel = koinViewModel()
        val feats by viewModel.feats.collectAsState(listOf())
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
                items(
                    feats,
                    key = { it.id }
                ) { feat ->
                    ListItem(
                        headlineText = {
                            Text(feat.name)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(FeatDetails.getNavRoute(feat.id))
                            })
                }
            }
        }
    }
}