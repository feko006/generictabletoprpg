package com.feko.generictabletoprpg.spell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpellOverviewViewModel(
    getAllSpellsUseCase: GetAllSpellsUseCase
) : ViewModel() {
    private val _spells =
        MutableStateFlow<List<Spell>>(listOf())
    val searchString: StateFlow<String>
        get() = _searchString
    private var _searchString: MutableStateFlow<String> = MutableStateFlow("")
    val spells: Flow<List<Spell>>
        get() = combinedSpellFlow
    private val combinedSpellFlow: Flow<List<Spell>> =
        _spells.combine(_searchString) { spells, searchString ->
            spells.filter { spell ->
                spell.name.lowercase().contains(searchString.lowercase())
            }
        }

    init {
        viewModelScope.launch {
            val allSpells =
                withContext(Dispatchers.Default) {
                    getAllSpellsUseCase.getAll()
                }
            _spells.emit(allSpells)
        }
    }

    fun searchStringUpdated(searchString: String) {
        viewModelScope.launch {
            _searchString.emit(searchString)
        }
    }
}