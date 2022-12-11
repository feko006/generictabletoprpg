package com.feko.generictabletoprpg.com.feko.generictabletoprpg.spell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.spell.GetSpellByIdUseCase
import com.feko.generictabletoprpg.spell.Spell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpellDetailsViewModel(
    private val getSpellByIdUseCase: GetSpellByIdUseCase
) : ViewModel() {
    val screenState: Flow<SpellDetailsScreenState>
        get() = _spell.map { spell ->
            if (spell == null) {
                SpellDetailsScreenState.Loading
            } else {
                SpellDetailsScreenState.SpellReady(spell)
            }
        }
    private val _spell: MutableStateFlow<Spell?> =
        MutableStateFlow(null)
    private var _spellId: Long = -1

    fun spellIdChanged(spellId: Long) {
        if (_spellId != spellId) {
            viewModelScope.launch {
                _spellId = spellId
                val newSpell =
                    withContext(Dispatchers.Default) {
                        getSpellByIdUseCase.invoke(_spellId)
                    }
                _spell.emit(newSpell)
            }
        }
    }

    sealed class SpellDetailsScreenState {
        object Loading : SpellDetailsScreenState()
        class SpellReady(val spell: Spell) : SpellDetailsScreenState()
    }
}