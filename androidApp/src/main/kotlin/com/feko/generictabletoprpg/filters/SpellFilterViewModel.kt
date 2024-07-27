package com.feko.generictabletoprpg.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.spell.ISpellFilterDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SpellFilterViewModel(
    spellFilterDao: ISpellFilterDao
) : ViewModel() {
    private val _schools = MutableStateFlow(listOf<String>())
    val schools: Flow<List<String>> = _schools
    private val _levels = MutableStateFlow(listOf<Int>())
    val levels: Flow<List<Int>> = _levels
    private val _classes = MutableStateFlow(listOf<String>())
    val classes: Flow<List<String>> = _classes

    init {
        viewModelScope.launch {
            spellFilterDao
                .getAllSchools()
                .map { schools ->
                    schools.map { school ->
                        school.replaceFirstChar { char ->
                            char.uppercase()
                        }
                    }
                }
                .collect(_schools)
        }
        viewModelScope.launch {
            spellFilterDao
                .getAllLevels()
                .map { it.sorted() }
                .collect(_levels)
        }
        viewModelScope.launch {
            spellFilterDao
                .getAllClasses()
                .map { classes ->
                    classes.map { it.split(", ") }
                }
                .map { classes ->
                    classes
                        .asSequence()
                        .flatten()
                        .filterNot { it.isBlank() }
                        .map { `class` -> `class`.replaceFirstChar(Char::uppercase) }
                        .distinct()
                        .sorted()
                        .toList()
                }
                .collect(_classes)
        }
    }
}