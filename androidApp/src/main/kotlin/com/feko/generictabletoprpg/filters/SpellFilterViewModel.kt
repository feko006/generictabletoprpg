package com.feko.generictabletoprpg.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.spell.IGetAllSpellSchools
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SpellFilterViewModel(
    spellSchoolDao: IGetAllSpellSchools
) : ViewModel() {
    private val _schools = MutableStateFlow(listOf<String>())
    val schools: Flow<List<String>> = _schools

    init {
        viewModelScope.launch {
            spellSchoolDao
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
    }
}