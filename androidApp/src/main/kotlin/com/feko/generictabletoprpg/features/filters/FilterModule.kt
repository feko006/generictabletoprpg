package com.feko.generictabletoprpg.features.filters

import com.feko.generictabletoprpg.features.filters.ui.SpellFilterViewModel
import com.feko.generictabletoprpg.features.spell.SpellDao
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val filterModule = module {
    viewModel { SpellFilterViewModel(get<SpellDao>()) }
}
