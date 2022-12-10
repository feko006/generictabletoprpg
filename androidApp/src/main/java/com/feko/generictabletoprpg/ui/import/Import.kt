package com.feko.generictabletoprpg.ui.import

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.feko.generictabletoprpg.ui.Navigation

object Import : Navigation.Destination {
    override val screenTitle: String
        get() = "Import"
    override val route: String
        get() = "import"
    override val isRootDestination: Boolean
        get() = true

    override fun navHostComposable(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appBarTitle: MutableState<String>
    ) {
        navGraphBuilder.composable(route) {
            appBarTitle.value = screenTitle
            Screen()
        }
    }

    @Composable
    private fun Screen() {

    }
}