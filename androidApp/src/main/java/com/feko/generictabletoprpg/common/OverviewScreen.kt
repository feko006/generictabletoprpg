package com.feko.generictabletoprpg.com.feko.generictabletoprpg.common

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
import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.Named

abstract class OverviewScreen<TViewModel, T> : Navigation.Destination
        where TViewModel : OverviewViewModel<*>,
              T : Identifiable,
              T : Named {
    abstract val detailsNavRouteProvider: Navigation.DetailsNavRouteProvider

    final override fun navHostComposable(
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
    fun Screen(navController: NavHostController) {
        val viewModel: TViewModel = getViewModel()
        val listItems by viewModel.items.collectAsState(listOf())
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
                    listItems,
                    key = { (it as Identifiable).id }
                ) { item ->
                    ListItem(
                        headlineText = {
                            Text(item.name)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    detailsNavRouteProvider.getNavRoute((item as Identifiable).id)
                                )
                            })
                }
            }
        }
    }

    @Composable
    abstract fun getViewModel(): TViewModel
}