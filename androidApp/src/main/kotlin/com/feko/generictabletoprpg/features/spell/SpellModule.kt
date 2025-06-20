package com.feko.generictabletoprpg.features.spell

import com.feko.generictabletoprpg.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.features.spell.ui.SpellDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val spellModule = module {
    single { get<GenericTabletopRpgDatabase>().spellDao() }
    viewModel { SpellDetailsViewModel(get<SpellDao>()) }
}
