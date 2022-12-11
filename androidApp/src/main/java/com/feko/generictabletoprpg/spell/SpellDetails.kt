package com.feko.generictabletoprpg.spell

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
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.spell.SpellDetailsViewModel
import org.koin.androidx.compose.koinViewModel


object SpellDetails : Navigation.Destination {
    private const val spellIdArgument = "id"
    private const val routeBase = "spellDetails"

    override val route: String
        get() = "$routeBase/{$spellIdArgument}"
    override val isRootDestination: Boolean
        get() = false
    override val screenTitle: String
        get() = "Spell Details"

    fun getNavRoute(spellId: Long): String {
        return "$routeBase/$spellId"
    }

    override fun navHostComposable(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appBarTitle: MutableState<String>
    ) {
        navGraphBuilder.composable(
            route = route,
            arguments = listOf(navArgument(spellIdArgument) {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            appBarTitle.value = screenTitle
            val spellId = backStackEntry.arguments!!.getLong(spellIdArgument)
            Screen(spellId)
        }
    }

    @Composable
    private fun Screen(spellId: Long) {
        val viewModel: SpellDetailsViewModel = koinViewModel()
        val screenState by viewModel.screenState.collectAsState(
            SpellDetailsViewModel.SpellDetailsScreenState.Loading
        )
        viewModel.spellIdChanged(spellId)
        when (screenState) {
            is SpellDetailsViewModel.SpellDetailsScreenState.Loading -> {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        Modifier
                            .size(100.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            is SpellDetailsViewModel.SpellDetailsScreenState.SpellReady -> {
                val readiedSpell =
                    screenState as SpellDetailsViewModel.SpellDetailsScreenState.SpellReady
                val padding = 8.dp
                Column(
                    Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    readiedSpell.spell.run {
                        TextWithLabel("Name", name)
                        Spacer(Modifier.height(padding))
                        TextWithLabel("Level", level.toString())
                        Spacer(Modifier.height(padding))
                        TextWithLabel("School", school)
                        Spacer(Modifier.height(padding))
                        TextWithLabel("Casting time", castingTime)
                        Spacer(Modifier.height(padding))
                        TextWithLabel("Range", range.toString())
                        if (hasComponents) {
                            Spacer(Modifier.height(padding))
                            TextWithLabel("Components", components.toString())
                        }
                        Spacer(Modifier.height(padding))
                        TextWithLabel("Duration", duration)
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