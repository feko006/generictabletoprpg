package com.feko.generictabletoprpg.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.theme.Typography

abstract class OverviewScreen<TViewModel, T> :
    Navigation.Destination
        where TViewModel : OverviewViewModel<T>,
              T : Any {
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
    private fun Screen(navController: NavHostController) {
        val viewModel: TViewModel = getViewModel()
        val listItems by viewModel.items.collectAsState(listOf())
        val searchString by viewModel.searchString.collectAsState("")
        Column(Modifier.padding(8.dp)) {
            Common.SearchTextField(searchString) {
                viewModel.searchStringUpdated(it)
            }
            Spacer(Modifier.height(8.dp))
            if (listItems.isNotEmpty()) {
                LazyColumn(
                    Modifier.fillMaxSize()
                ) {
                    items(
                        listItems,
                        key = { listItem -> uniqueListItemKey(listItem) }
                    ) { item ->
                        ListItem(
                            headlineContent = {
                                Text((item as Named).name)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        getNavRouteInternal(item)
                                    )
                                })
                    }
                }
            } else {
                Box(Modifier.fillMaxSize()) {
                    Column(
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    ) {
                        Icon(
                            Icons.Default.List, "",
                            Modifier
                                .size(80.dp)
                                .align(CenterHorizontally)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Nothing here...",
                            Modifier.align(CenterHorizontally),
                            style = Typography.titleLarge
                        )
                    }
                }
            }
        }
    }

    protected open fun uniqueListItemKey(listItem: T): Any = (listItem as Identifiable).id

    protected open fun getNavRouteInternal(item: T) =
        detailsNavRouteProvider.getNavRoute((item as Identifiable).id)

    @Composable
    protected abstract fun getViewModel(): TViewModel
}