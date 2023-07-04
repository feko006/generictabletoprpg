package com.feko.generictabletoprpg.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.feko.generictabletoprpg.ButtonState
import com.feko.generictabletoprpg.Navigation

abstract class DetailsScreen<TViewModel, T> :
    Navigation.Destination,
    Navigation.DetailsNavRouteProvider
        where TViewModel : DetailsViewModel<T> {
    abstract val routeBase: String

    private val idArgumentName = "id"
    final override val route: String
        get() = "${routeBase}/{$idArgumentName}"

    final override fun getNavRoute(id: Long): String = "${routeBase}/$id"

    final override fun navHostComposable(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appBarTitle: MutableState<String>,
        setNavBarActions: (List<ButtonState>) -> Unit
    ) {
        navGraphBuilder.composable(
            route = route,
            arguments = listOf(navArgument(idArgumentName) {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            appBarTitle.value = screenTitle
            setNavBarActions(listOf())
            val id = backStackEntry.arguments!!.getLong(idArgumentName)
            Screen(id)
        }
    }

    @Composable
    private fun Screen(id: Long) {
        val viewModel: TViewModel = getViewModel()
        val screenState by viewModel.screenState.collectAsState(
            DetailsViewModel.DetailsScreenState.Loading
        )
        viewModel.featIdChanged(id)
        when (screenState) {
            is DetailsViewModel.DetailsScreenState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            is DetailsViewModel.DetailsScreenState.ItemReady<*> -> {
                @Suppress("UNCHECKED_CAST")
                val readiedItem =
                    screenState as DetailsViewModel.DetailsScreenState.ItemReady<T>
                val padding = 8.dp
                Column(
                    Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    ScreenContent(readiedItem, padding)
                }
            }
        }
    }

    @Composable
    protected abstract fun ScreenContent(
        readiedItem: DetailsViewModel.DetailsScreenState.ItemReady<T>,
        padding: Dp
    )

    @Composable
    protected abstract fun getViewModel(): TViewModel
}