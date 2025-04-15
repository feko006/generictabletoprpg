package com.feko.generictabletoprpg.initiative

import androidx.lifecycle.ViewModel
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.initiative.InitiativeEntryEntity
import kotlinx.coroutines.flow.Flow

class InitiativeViewModel(private val dao: InitiativeEntryDao) : ViewModel() {
    val entries: Flow<List<InitiativeEntryEntity>>
        get() = dao.getAllSortedByInitiative()
}

