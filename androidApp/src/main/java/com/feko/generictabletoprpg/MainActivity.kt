package com.feko.generictabletoprpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.feko.generictabletoprpg.ui.Navigation
import com.feko.generictabletoprpg.ui.spell.SpellOverview
import com.feko.generictabletoprpg.ui.spell.fivee.Spell
import com.feko.generictabletoprpg.ui.theme.GenerictabletoprpgTheme
import com.squareup.moshi.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val spells = mutableListOf<Spell>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        spells.add(Spell(name = "Spell 1", level = 1))
        spells.add(Spell(name = "Spell 2", level = 2))
        spells.add(Spell(name = "Spell 3", level = 3))

        setContent {
            GenerictabletoprpgTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Title") },
                            navigationIcon = {
                                Icon(
                                    Icons.Default.Menu,
                                    "",
                                    Modifier.clickable {
                                        coroutineScope.launch {
                                            if (drawerState.isOpen) {
                                                drawerState.close()
                                            } else {
                                                drawerState.open()
                                            }
                                        }
                                    })
                            })
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            navController.navigate(SpellOverview.route)
                        }) {
                            Icon(Icons.Default.List, "")
                        }
                    }) {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                Text("1")
                                Text("2")
                                Text("3")
                            }
                        },
                        modifier = Modifier.padding(it)
                    ) {
                        Navigation.Host(navController, spells)
                    }
                }
            }
        }
    }

}