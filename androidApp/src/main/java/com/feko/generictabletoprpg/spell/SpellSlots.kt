package com.feko.generictabletoprpg.spell

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.feko.generictabletoprpg.Navigation
import com.feko.generictabletoprpg.rememberSaveableMutableStateIntToIntMap

object SpellSlots : Navigation.Destination {
    override val route: String
        get() = "spellSlots"
    override val isRootDestination: Boolean
        get() = true
    override val screenTitle: String
        get() = "Spell Slots"

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
        val slots = rememberSaveableMutableStateIntToIntMap("slots")
        val usedSlots = rememberSaveableMutableStateIntToIntMap("usedSlots")
        var slotLevelDropdownExpanded by remember { mutableStateOf(false) }
        var slotLevel by remember { mutableStateOf(0) }

        Column {
            Button(onClick = {
                slotLevelDropdownExpanded = !slotLevelDropdownExpanded
            }) {
                Text(if (slotLevel == 0) "Slot level" else "Slot level $slotLevel")
                DropdownMenu(
                    expanded = slotLevelDropdownExpanded,
                    onDismissRequest = { },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    for (i in 1..9) {
                        DropdownMenuItem(
                            text = { Text("$i") },
                            onClick = {
                                slotLevel = i
                                slotLevelDropdownExpanded = false
                            })
                    }
                }
            }
            var slotNumberDropdownExpanded by remember {
                mutableStateOf(
                    false
                )
            }
            var slotNumber by remember { mutableStateOf(0) }
            Button(onClick = {
                slotNumberDropdownExpanded = !slotNumberDropdownExpanded
            }) {
                Text(if (slotNumber == 0) "Number of slots" else "$slotNumber slots")
                DropdownMenu(
                    expanded = slotNumberDropdownExpanded,
                    onDismissRequest = { },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    for (i in 1..9) {
                        DropdownMenuItem(
                            text = { Text("$i") },
                            onClick = {
                                slotNumber = i
                                slotNumberDropdownExpanded = false
                            })
                    }
                }
            }
            Button(
                onClick = {
                    slots[slotLevel] = slotNumber
                    if (usedSlots.getOrDefault(
                            slotLevel,
                            0
                        ) > slotNumber
                    ) {
                        usedSlots[slotLevel] = slotNumber
                    }
                    slotLevel = 0
                    slotNumber = 0
                },
                enabled = slotLevel > 0 && slotNumber > 0
            ) {
                Text("Add")
            }
            Button(onClick = {
                usedSlots.clear()
            }) {
                Text("Reset usage")
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            slots.forEach {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val used = usedSlots.getOrDefault(it.key, 0)
                    val total = it.value
                    val available = total - used
                    Text("Available Level ${it.key} Slots: $available/$total")
                    Button(
                        onClick = {
                            usedSlots[it.key] = used + 1
                        },
                        enabled = available > 0
                    ) {
                        Text("Use")
                    }
                }
            }
        }
    }
}