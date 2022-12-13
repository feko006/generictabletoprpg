package com.feko.generictabletoprpg.feat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.feko.generictabletoprpg.Navigation
import org.koin.androidx.compose.koinViewModel

object FeatDetails :
    Navigation.Destination,
    Navigation.DetailsNavRouteProvider {
    private const val routeBase = "featDetails"
    private const val featIdArgument = "id"

    override val screenTitle: String
        get() = "Feat Details"
    override val route: String
        get() = "$routeBase/{$featIdArgument}"
    override val isRootDestination: Boolean
        get() = false

    override fun getNavRoute(id: Long): String = "$routeBase/$id"

    override fun navHostComposable(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appBarTitle: MutableState<String>
    ) {
        navGraphBuilder.composable(
            route = route,
            arguments = listOf(navArgument(featIdArgument) {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            appBarTitle.value = screenTitle
            val featId = backStackEntry.arguments!!.getLong(featIdArgument)
            Screen(featId)
        }
    }

    @Composable
    private fun Screen(featId: Long) {
        val viewModel: FeatDetailsViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsState(
            FeatDetailsViewModel.FeatDetailsScreenState.Loading
        )
        viewModel.featIdChanged(featId)
        when (screenState) {
            is FeatDetailsViewModel.FeatDetailsScreenState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            is FeatDetailsViewModel.FeatDetailsScreenState.FeatReady -> {
                val readiedFeat =
                    screenState as FeatDetailsViewModel.FeatDetailsScreenState.FeatReady
                val padding = 8.dp
                Column(
                    Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    readiedFeat.feat.run {
                        TextWithLabel("Name", name)
                        if (hasRequirements) {
                            Spacer(Modifier.height(padding))
                            TextWithLabel("Requirements", requirements)
                        }
                        Divider(Modifier.padding(vertical = padding))
                        Text(description)
                    }
                }
            }
        }
    }

    @Composable
    private fun TextWithLabel(
        label: String,
        text: String
    ) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(label)
                }
                append(": $text")
            }
        )
    }
}