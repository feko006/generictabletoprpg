package com.feko.generictabletoprpg

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.feko.generictabletoprpg.spell.fiveetools.Spell
import com.feko.generictabletoprpg.theme.GenerictabletoprpgTheme
import com.squareup.moshi.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        val spells = mutableListOf<Spell>()
        lateinit var appContext: Context
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext

        spells.add(Spell(name = "Spell 1", level = 1))
        spells.add(Spell(name = "Spell 2", level = 2))
        spells.add(Spell(name = "Spell 3", level = 3))

        setContent {
            GenerictabletoprpgTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()
                val appBarTitle = rememberSaveable { mutableStateOf("") }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(appBarTitle.value)
                            },
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
                    }) { paddingValues ->
                    Navigation.Drawer(drawerState, paddingValues, navController, appBarTitle)
                }
            }
        }
    }
}