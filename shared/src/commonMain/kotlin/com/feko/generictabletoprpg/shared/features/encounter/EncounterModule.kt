package com.feko.generictabletoprpg.shared.features.encounter

import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.encounter.ui.EncounterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val encounterModule = module {
    single<InitiativeEntryDao> { get<GenericTabletopRpgDatabase>().initiativeEntryDao() }
    viewModelOf(::EncounterViewModel)
}