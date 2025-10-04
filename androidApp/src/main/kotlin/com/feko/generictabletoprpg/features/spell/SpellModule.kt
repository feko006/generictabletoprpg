package com.feko.generictabletoprpg.features.spell

import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.features.spell.ui.SpellDetailsViewModel
import com.feko.generictabletoprpg.shared.features.spell.SpellDao
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val spellModule = module {
    single { get<GenericTabletopRpgDatabase>().spellDao() }
    viewModel { SpellDetailsViewModel(get<SpellDao>()) }
}
