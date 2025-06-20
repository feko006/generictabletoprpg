package com.feko.generictabletoprpg.features.encounter

import com.feko.generictabletoprpg.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.features.encounter.ui.EncounterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val encounterModule = module {
    single<InitiativeEntryDao> { get<GenericTabletopRpgDatabase>().initiativeEntryDao() }
    viewModelOf(::EncounterViewModel)
}
